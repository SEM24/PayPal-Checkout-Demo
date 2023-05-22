package com.example.paypal.payment.service;

import com.example.paypal.payment.model.CompletedOrder;
import com.example.paypal.payment.model.PaymentOrder;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface PayPalService {
    PaymentOrder createPayment(BigDecimal amount, HttpServletRequest request);

    CompletedOrder completePayment(String token);
}
