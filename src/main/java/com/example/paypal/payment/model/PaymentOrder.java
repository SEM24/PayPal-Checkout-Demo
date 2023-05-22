package com.example.paypal.payment.model;

import lombok.*;

import java.io.Serializable;

@Builder
public record PaymentOrder(
        String status,
        String payId,
        String redirectUrl) implements Serializable {
}

