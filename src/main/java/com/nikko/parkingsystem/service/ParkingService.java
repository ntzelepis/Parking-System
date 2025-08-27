package com.nikko.parkingsystem.service;

import com.nikko.parkingsystem.dto.ParkingSummaryDTO;
import com.nikko.parkingsystem.model.*;
import com.nikko.parkingsystem.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingRepository parkingRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingZoneRepository zoneRepository;
    private final ParkingSpaceRepository spaceRepository;
    private final PriceListRepository priceListRepository;

    public List<ParkingSummaryDTO> getAllParkingSummaries() {
        return parkingRepository.findAll().stream()
                .map(parking -> {
                    ParkingSummaryDTO dto = new ParkingSummaryDTO();
                    dto.setId(parking.getId());
                    dto.setName(parking.getName());
                    dto.setZoneCount(parking.getZones() != null ? parking.getZones().size() : 0);
                    return dto;
                })
                .toList();
    }


    public String getParkingNameById(Long parkingId) {
        return parkingRepository.findById(parkingId)
                .map(Parking::getName)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Parking getParkingById(Long parkingId) {
        return parkingRepository.findById(parkingId)
                .orElseThrow(() -> new IllegalArgumentException("Parking not found for ID: " + parkingId));
    }


    @Transactional(readOnly = true)
    public Parking getParkingByName(String parkingName) {
        return parkingRepository.findByName(parkingName).orElse(null);
    }

    @Transactional
    public void createParkingWithZones(String parkingName, int zoneCount, Long priceListId) {
        if (parkingRepository.findByName(parkingName).isPresent()) return;

        Parking parking = new Parking();
        parking.setName(parkingName);

        PriceList priceList = priceListRepository.findById(priceListId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid price list ID"));
        parking.setPriceList(priceList);

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
    public void addValidatedSpaceToZone(Long parkingId, Long zoneId, int count, SpaceType type) {
        ParkingZone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new IllegalArgumentException("Zone not found for ID: " + zoneId));

        if (!zone.getParking().getId().equals(parkingId)) {
            throw new IllegalArgumentException("Zone does not belong to the specified parking ID: " + parkingId);
        }

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
