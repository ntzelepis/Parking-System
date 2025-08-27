package com.nikko.parkingsystem.repo;

import com.nikko.parkingsystem.model.ParkingSpace;
import com.nikko.parkingsystem.model.ParkingZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {

    List<ParkingSpace> findByZoneId(Long zoneId);

    List<ParkingSpace> findByParkingName(String parkingName);

    List<ParkingSpace> findAvailableByParkingName(String parkingName);

    long countByZone(ParkingZone zone);

    int countByZoneId(Long zoneId);
}

