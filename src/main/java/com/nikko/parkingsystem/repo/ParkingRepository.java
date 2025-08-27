package com.nikko.parkingsystem.repo;

import com.nikko.parkingsystem.model.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParkingRepository extends JpaRepository<Parking, Long> {

    @Query("""
        SELECT p FROM Parking p
        LEFT JOIN FETCH p.zones
        WHERE p.name = :name
    """)
    Optional<Parking> findByNameWithZones(@Param("name") String name);

    Optional<Parking> findByName(String name);
}

