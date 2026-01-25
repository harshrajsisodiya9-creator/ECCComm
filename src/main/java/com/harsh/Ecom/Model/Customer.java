package com.harsh.Ecom.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)    User id will be used as a primary key(foreign key as primary key here) for identification
    private Long id;

    @OneToOne
    @MapsId             // maps the customer id to its user id(meaning user id is there in customer class)
    private User user;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
