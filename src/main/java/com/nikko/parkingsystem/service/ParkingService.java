package com.nikko.parkingsystem.service;

import com.nikko.parkingsystem.model.*;
import com.nikko.parkingsystem.repo.ParkingRepository;
import com.nikko.parkingsystem.repo.ParkingSpaceRepository;
import com.nikko.parkingsystem.repo.ParkingZoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkingService {

    private final ParkingRepository parkingRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingZoneRepository zoneRepository;
    private final ParkingSpaceRepository spaceRepository;

    public ParkingService(ParkingRepository parkingRepository,
                          ParkingSpaceRepository parkingSpaceRepository, ParkingZoneRepository zoneRepository, ParkingSpaceRepository spaceRepository) {
        this.parkingRepository = parkingRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.zoneRepository = zoneRepository;
        this.spaceRepository = spaceRepository;
    }

    public List<Map<String, Object>> getAllParkingSummaries() {
        return parkingRepository.findAll()
                .stream()
                .map(parking -> {
                    Map<String, Object> summary = new HashMap<>();
                    summary.put("name", parking.getName());
                    summary.put("zoneCount", parking.getZones() != null ? parking.getZones().size() : 0);
                    return summary;
                })
                .toList();
    }

    public String getParkingNameById(Long parkingId) {
        return parkingRepository.findById(parkingId)
                .map(Parking::getName)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Parking getParkingByName(String parkingName) {
        return parkingRepository.findByName(parkingName)
                .orElse(null);
    }

    @Transactional
    public void createParkingWithZones(String parkingName, int zoneCount) {
        if (parkingRepository.findByName(parkingName).isPresent()) {
            return;
        }

        Parking parking = new Parking();
        parking.setName(parkingName);

        List<ParkingZone> zones = new ArrayList<>();
        for (int i = 0; i < zoneCount; i++) {
            char zoneLetter = (char) ('A' + i);
            ParkingZone zone = new ParkingZone();
            zone.setZoneLetter("Zone " + zoneLetter);
            zone.setParking(parking);
            zones.add(zone);
        }

        parking.setZones(zones);
        parkingRepository.save(parking);
    }

    private String toAlphabeticZoneName(int number) {
        StringBuilder result = new StringBuilder();
        while (number > 0) {
            number--;
            result.insert(0, (char) ('A' + (number % 26)));
            number /= 26;
        }
        return result.toString();
    }

    @Transactional
    public void addValidatedSpaceToZone(String parkingName, String zoneLetter, int count, SpaceType type) {
        ParkingZone zone = zoneRepository.findByParkingNameAndZoneLetter(parkingName, zoneLetter)
                .orElseThrow(() -> new IllegalArgumentException("Zone not found for parking: " + parkingName + ", zone: " + zoneLetter));

        String normalizedZoneLetter = zone.getZoneLetter().replace("Zone", "").trim();

        long existingCount = spaceRepository.countByZone(zone);

        for (int i = 1; i <= count; i++) {
            long index = existingCount + i;
            String spaceId = normalizedZoneLetter + index;

            ParkingSpace space = new ParkingSpace();
            space.setZone(zone);
            space.setZoneName(normalizedZoneLetter);
            space.setType(type);
            space.setOccupied(false);
            space.setParkingName(zone.getParking().getName());
            space.setSpaceCode(spaceId);
            spaceRepository.save(space);
        }


    }


}