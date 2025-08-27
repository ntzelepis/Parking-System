package com.nikko.parkingsystem.model;

import jakarta.persistence.*;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private double amount;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "parking_space_id", nullable = false)
    private ParkingSpace parkingSpace;

    @OneToOne
    @JoinColumn(name = "session_id", nullable = false)
    private ParkingSession session;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parking;


    protected Payment() {}

    public Payment(Vehicle vehicle, ParkingSession session, double amount) {
        this.vehicle = vehicle;
        this.session = session;
        this.amount = amount;
        this.parkingSpace = session.getParkingSpace();
    }

    public Long getId() { return id; }
    public double getAmount() { return amount; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpace getParkingSpace() { return parkingSpace; }
    public ParkingSession getSession() { return session; }
    public Parking getParking() { return parking; }


    public void setId(Long id) { this.id = id; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public void setParkingSpace(ParkingSpace parkingSpace) { this.parkingSpace = parkingSpace; }
    public void setSession(ParkingSession session) { this.session = session; }
    public void setParking(Parking parking) { this.parking = parking; }


    public String getParkingName() {
        return parkingSpace != null ? parkingSpace.getParkingName() : null;
    }
}
