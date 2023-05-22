package com.example.paypal.payment.controller;

import com.example.paypal.payment.service.PayPalServiceImpl;
import com.example.paypal.payment.model.CompletedOrder;
import com.example.paypal.payment.model.PaymentOrder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/paypal")
@AllArgsConstructor
public class PaymentController {
    private PayPalServiceImpl paypalServiceImpl;

    @PostMapping(value = "/checkout")
    public PaymentOrder createPayment(
            @RequestParam("sum") BigDecimal sum, HttpServletRequest request) {
        return paypalServiceImpl.createPayment(sum, request);
    }

    @PostMapping(value = "/capture")
    public CompletedOrder completePayment(
            @RequestParam("token") String token) {
        return paypalServiceImpl.completePayment(token);
    }
}
