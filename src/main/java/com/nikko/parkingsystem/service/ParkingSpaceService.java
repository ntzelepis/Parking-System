package com.nikko.parkingsystem.service;

import com.nikko.parkingsystem.model.ParkingSpace;
import com.nikko.parkingsystem.model.SpaceType;
import com.nikko.parkingsystem.repo.ParkingSpaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;

    public ParkingSpaceService(ParkingSpaceRepository parkingSpaceRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    public double getZoneOccupancyPercentage(Long zoneId) {
        List<ParkingSpace> allSpaces = parkingSpaceRepository.findByZoneId(zoneId);
        long occupied = allSpaces.stream()
                .filter(ParkingSpace::isOccupied)
                .count();

        int total = allSpaces.size();
        if (total == 0) return 0.0;
        return Math.round(((double) occupied / total) * 1000.0) / 10.0;
    }

    public int getAvailableSpaceCount(String parkingName, String zoneName) {
        return (int) parkingSpaceRepository.findAvailableByParkingName(parkingName).stream()
                .filter(space -> space.getZoneName().equals(zoneName))
                .count();
    }

    public String isSpaceTypeAvailable(Long parkingId, Long zoneId, SpaceType type) {
        List<ParkingSpace> availableSpaces = parkingSpaceRepository.findByZoneId(zoneId).stream()
                .filter(space -> space.getType() != null && space.getType().equals(type))
                .filter(space -> !space.isOccupied())
                .collect(Collectors.toList());

        int count = availableSpaces.size();
        if (count > 0) {
            return "There are " + count + " available spaces of " + type + " type.";
        } else {
            return "There are no available spaces of " + type + " type at the moment.";
        }
    }

    public int getZoneSpaceCount(Long parkingId, Long zoneId) {
        return parkingSpaceRepository.countByZoneId(zoneId);
    }

    public int getOccupiedSpaceCount(String parkingName, String zoneName) {
        return (int) parkingSpaceRepository.findByParkingName(parkingName).stream()
                .filter(space -> space.getZoneName().equals(zoneName) && space.isOccupied())
                .count();
    }
}
