package com.nikko.parkingsystem.repo;

import com.nikko.parkingsystem.model.ParkingSession;
import com.nikko.parkingsystem.model.Payment;
import com.nikko.parkingsystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByVehicle(Vehicle vehicle);

    @Query("SELECT p FROM Payment p JOIN FETCH p.session s JOIN FETCH s.parkingSpace WHERE s = :session")
    Optional<Payment> findBySessionWithDetails(@Param("session") ParkingSession session);

}
