package com.nikko.parkingsystem.repo;

import com.nikko.parkingsystem.model.ParkingSpace;
import com.nikko.parkingsystem.model.ParkingZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {

    int countByParkingNameAndZoneName(String parkingName, String zoneName);

    @Query("SELECT s FROM ParkingSpace s WHERE s.parkingName = :parkingName AND s.occupied = false")
    List<ParkingSpace> findAvailableByParkingName(@Param("parkingName") String parkingName);

    List<ParkingSpace> findByZoneName(String zoneName);

    List<ParkingSpace> findByZoneId(Long zoneId);

    @Query("SELECT s FROM ParkingSpace s WHERE s.parkingName = :parkingName")
    List<ParkingSpace> findByParkingName(@Param("parkingName") String parkingName);
    long countByZone(ParkingZone zone);


}

