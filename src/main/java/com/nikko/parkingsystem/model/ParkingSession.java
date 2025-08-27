package com.nikko.parkingsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parking_space_id")
    private ParkingSpace parkingSpace;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_time")
    private LocalDateTime end;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private Payment payment;


    public double getDurationHours() {
        if (start == null || end == null) return 0;
        Duration duration = Duration.between(start, end);
        return duration.toMinutes() / 60.0;
    }


    public boolean isActive() {
        return end == null;
    }
}

