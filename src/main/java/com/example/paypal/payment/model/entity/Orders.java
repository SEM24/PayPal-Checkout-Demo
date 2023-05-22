package com.example.paypal.payment.model.entity;

import com.example.paypal.payment.model.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String orderId;

    private String status;

    private String link;

    private LocalDate createDate;

    private LocalDate updateDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "activation")
    private OrderStatus activation;
}
