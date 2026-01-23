package com.harsh.Ecom.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @MapsId             // maps the customer id to its user id(matlab user id is there in customer class)
    private User user;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
