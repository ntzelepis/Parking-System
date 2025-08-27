package com.nikko.parkingsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "pricing_category")
public class PricingCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double minDuration;
    private double maxDuration;
    private double billingScale;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parking;


}