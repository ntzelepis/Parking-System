package com.nikko.parkingsystem.repo;

import com.nikko.parkingsystem.model.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
    Optional<Parking> findByName(String name);

    @Query("""
    SELECT p FROM Parking p
    LEFT JOIN FETCH p.zones
    WHERE p.id = :id
""")
    Optional<Parking> findByIdWithZones(@Param("id") Long id);

}
