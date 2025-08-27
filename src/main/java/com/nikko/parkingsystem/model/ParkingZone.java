package com.nikko.parkingsystem.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "parking_zone")
public class ParkingZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String zoneLetter;

    @ManyToOne
    @JoinColumn(name = "parking_id", nullable = false)
    private Parking parking;


}
