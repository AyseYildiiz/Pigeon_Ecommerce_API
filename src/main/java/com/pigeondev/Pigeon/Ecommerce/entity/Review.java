package com.pigeondev.Pigeon.Ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private int rating; // assuming it is 1 to 10

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt= LocalDateTime.now();

}
