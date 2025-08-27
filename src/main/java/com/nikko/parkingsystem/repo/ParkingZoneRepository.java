package com.nikko.parkingsystem.repo;

import com.nikko.parkingsystem.model.ParkingZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingZoneRepository extends JpaRepository<ParkingZone, Long> {
    List<ParkingZone> findByParkingName(String parkingName);
}
