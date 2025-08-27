package com.nikko.parkingsystem.repo;

import com.nikko.parkingsystem.model.ParkingZone;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;
import java.util.Optional;


public interface ParkingZoneRepository extends JpaRepository<ParkingZone, Long> {
    List<ParkingZone> findByParkingName(String parkingName);
    Optional<ParkingZone> findByParkingNameAndZoneLetter(String parkingName, String zoneLetter);
    Optional<ParkingZone> findByParking_NameAndZoneLetter(String parkingName, String zoneLetter);

}

