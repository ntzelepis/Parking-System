package com.nikko.parkingsystem.repo;

import com.nikko.parkingsystem.model.ParkingSession;
import com.nikko.parkingsystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {

    List<ParkingSession> findByVehicle(Vehicle vehicle);

    @Query("""
        SELECT s FROM ParkingSession s
        WHERE s.vehicle = :vehicle AND s.end IS NULL
        ORDER BY s.start DESC
    """)
    Optional<ParkingSession> findActiveSession(@Param("vehicle") Vehicle vehicle);
}
