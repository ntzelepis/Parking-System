package com.nikko.parkingsystem.controller;

import com.nikko.parkingsystem.model.SpaceType;
import com.nikko.parkingsystem.repo.ParkingSpaceRepository;
import com.nikko.parkingsystem.service.ParkingSpaceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spaces")
public class ParkingSpaceController {

    private final ParkingSpaceService spaceService;
    private final ParkingSpaceRepository parkingSpaceRepository;

    public ParkingSpaceController(ParkingSpaceService spaceService, ParkingSpaceRepository parkingSpaceRepository) {
        this.spaceService = spaceService;
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    @GetMapping("/{parkingName}/{zoneName}/occupancy")
    public String getZoneOccupancy(@PathVariable String parkingName,
                                   @PathVariable String zoneName) {
        Long zoneId = parkingSpaceRepository.findByZoneName(zoneName).get(0).getZone().getId();
        double percentage = spaceService.getZoneOccupancyPercentage(zoneId);
        return percentage + "%";
    }

    @GetMapping("/{parkingName}/{zoneName}/available/{type}")
    public String isSpaceTypeAvailable(@PathVariable String parkingName,
                                       @PathVariable String zoneName,
                                       @PathVariable SpaceType type) {
        return spaceService.isSpaceTypeAvailable(parkingName, zoneName, type);
    }

    @GetMapping("/{parkingName}/{zoneLetter}/count")
    public int getZoneSpaceCount(@PathVariable String parkingName,
                                 @PathVariable String zoneLetter) {
        return spaceService.getZoneSpaceCount(parkingName, zoneLetter);
    }
}