package com.example.paypal.payment.service;

import com.example.paypal.payment.OrdersRepository;
import com.example.paypal.config.PaypalConfiguration;
import com.example.paypal.payment.model.CompletedOrder;
import com.example.paypal.payment.model.PaymentOrder;
import com.example.paypal.payment.model.entity.Orders;
import com.example.paypal.payment.model.enums.OrderStatus;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.example.paypal.payment.model.PayPalEndpoints.*;

@Slf4j
@Service
@AllArgsConstructor
public class PayPalServiceImpl implements PayPalService {

    private PayPalHttpClient payPalHttpClient;
    private PaypalConfiguration paypalConfig;
    private OrdersRepository ordersRepo;

    @Override
    public PaymentOrder createPayment(BigDecimal amount, HttpServletRequest request) {
        OrdersCreateRequest createRequest = new OrdersCreateRequest();
        createRequest.requestBody(createOrderRequest(amount, request));
        try {
            HttpResponse<Order> response = payPalHttpClient.execute(createRequest);
            Order order = response.result();

            String redirectUrl = order.links().stream()
                    .filter(link -> OrderStatus.APPROVE.name().equalsIgnoreCase(link.rel()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No approved link found in response"))
                    .href();

            Orders saveOrder = Orders.builder()
                    .orderId(order.id()) //token
                    .status(order.status())
                    .createDate(LocalDate.now())
                    .activation(OrderStatus.PAYER_ACTION_REQUIRED)
                    .link(redirectUrl)
                    .build();
            ordersRepo.save(saveOrder);
            return new PaymentOrder(OrderStatus.SUCCESS.toString(), order.id(), redirectUrl);
        } catch (IOException e) {
            return PaymentOrder.builder()
                    .status(e.getLocalizedMessage())
                    .build();
        }
    }

    @Override
    public CompletedOrder completePayment(String token) {
        OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(token);
        try {
            HttpResponse<Order> httpResponse = payPalHttpClient.execute(ordersCaptureRequest);
            if (httpResponse.result().status() != null) {
                Orders oldOrder = ordersRepo.findByOrderId(token);
                oldOrder.setActivation(OrderStatus.READY_TO_USE);
                oldOrder.setStatus(OrderStatus.COMPLETED.name());
                oldOrder.setUpdateDate(LocalDate.now());
                oldOrder.setLink(OrderStatus.SUCCESS.toString());
                ordersRepo.save(oldOrder);
                return new CompletedOrder(OrderStatus.SUCCESS.toString(), token);
            }
        } catch (IOException e) {
            log.error("Failed to capture payment with PayPal", e);
        }
        return new CompletedOrder(OrderStatus.FAILED.toString(), token);
    }

    private OrderRequest createOrderRequest(BigDecimal amount, HttpServletRequest url) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        AmountWithBreakdown amountBreakdown = new AmountWithBreakdown()
                .currencyCode(paypalConfig.currencyCode())
                .value(amount.toString());

        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .amountWithBreakdown(amountBreakdown);

        orderRequest.purchaseUnits(List.of(purchaseUnitRequest));

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(createUrl(url, CAPTURE))
                .cancelUrl(createUrl(url, CANCEL));

        orderRequest.applicationContext(applicationContext);

        return orderRequest;
    }
}

