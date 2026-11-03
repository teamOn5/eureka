package com.example.spring.test.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
//@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long price;

    private boolean deleted = false;

    public void changePriceByAdmin(Long price) {
        this.price = price;
    }

    public void softDeleteByUser() {
        this.deleted = true;
    }
}
