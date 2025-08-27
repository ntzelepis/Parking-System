package com.nikko.parkingsystem.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "price_list")
@Data
@NoArgsConstructor
public class PriceList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double baseRate;

    @OneToOne(mappedBy = "priceList")
    private Parking parking;

    public PriceList(double baseRate) {
        this.baseRate = baseRate;
    }
}
