package com.nikko.parkingsystem.service;

import com.nikko.parkingsystem.model.Parking;
import com.nikko.parkingsystem.model.ParkingZone;
import com.nikko.parkingsystem.repo.ParkingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingZoneService {

    private final ParkingRepository parkingRepository;

    public ParkingZoneService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    public List<String> getZoneNamesByParkingId(Long parkingId) {
        Parking parking = parkingRepository.findByIdWithZones(parkingId).orElse(null);
        if (parking == null) {
            return List.of();
        }
        return parking.getZones().stream()
                .map(ParkingZone::getZoneLetter)
                .collect(Collectors.toList());
    }
}
