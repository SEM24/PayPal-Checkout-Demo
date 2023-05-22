package com.example.paypal.payment.model;

import lombok.*;

@Builder
public record CompletedOrder(String status, String payId) {
}
