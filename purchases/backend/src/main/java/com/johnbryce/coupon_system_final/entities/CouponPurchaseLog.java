package com.johnbryce.coupon_system_final.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponPurchaseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String company;
    private String category;
    private String description;
    private LocalDate purchaseDate;
    private String customerFirstName;
    private String customerLastName;
    private UUID token;
}
