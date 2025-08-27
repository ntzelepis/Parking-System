package com.nikko.parkingsystem.repo;

import com.nikko.parkingsystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Vehicle findByLicensePlate(String licensePlate);

    @Query("""
    SELECT v FROM Vehicle v
    LEFT JOIN FETCH v.sessions
    WHERE v.licensePlate = :licensePlate
""")
    Optional<Vehicle> findByLicensePlateWithSessions(@Param("licensePlate") String licensePlate);

}
