package com.nikko.parkingsystem.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "price_list")
@Data
@NoArgsConstructor
public class PriceList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double baseRate;

    @OneToMany(mappedBy = "priceList")
    private List<Parking> parkings;


    public PriceList(double baseRate) {
        this.baseRate = baseRate;
    }
}
