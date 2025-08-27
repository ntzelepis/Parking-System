package com.nikko.parkingsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Data
@Getter
@NoArgsConstructor
public class ParkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "space_id", nullable = true)
    private String spaceCode;

    @Column(name = "parking_name", nullable = false)
    private String parkingName;

    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private ParkingZone zone;

    @Column(name = "zone_name", nullable = false)
    private String zoneName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SpaceType type;

    @Column(name = "occupied", nullable = false)
    private boolean occupied;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    public ParkingSpace(String parkingName, String normalizedZoneName, SpaceType spaceType, boolean occupied) {
        this.parkingName = parkingName;
        this.zoneName = normalizedZoneName;
        this.type = spaceType;
        this.occupied = occupied;
    }

    public boolean isOccupied() {
        return Boolean.TRUE.equals(this.occupied);


    }
}