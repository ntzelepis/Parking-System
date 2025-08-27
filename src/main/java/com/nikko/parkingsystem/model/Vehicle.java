package com.nikko.parkingsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "model")
    private String model;

    @NonNull
    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private VehicleType type;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ParkingSession> sessions = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "parking_space_id")
    private ParkingSpace parkingSpace;

    public Vehicle(String model, String licensePlate) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.type = VehicleType.CAR;
    }

    public Vehicle(String licensePlate) {
        this.licensePlate = licensePlate;
        this.model = "Unknown";
        this.type = VehicleType.CAR;
    }

    public void startSession(ParkingSpace space, LocalDateTime time) {
        ParkingSession session = new ParkingSession();
        session.setVehicle(this);
        session.setParkingSpace(space);
        session.setStart(time);
        sessions.add(session);
    }

    public void endSession(LocalDateTime endTime) {
        ParkingSession currentSession = getCurrentSession();
        if (currentSession != null) {
            currentSession.setEnd(endTime);
            currentSession.getParkingSpace().setOccupied(false);
        }
    }

    public ParkingSession getCurrentSession() {
        return sessions.stream()
                .filter(ParkingSession::isActive)
                .findFirst()
                .orElse(null);
    }

    public void clearSessions() {
        sessions.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle that = (Vehicle) o;
        return licensePlate.equals(that.licensePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licensePlate);
    }
}
