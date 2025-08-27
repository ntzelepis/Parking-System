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

    @GetMapping("/{parking_id}/{zone_id}/occupancy")
    public String getZoneOccupancy(@PathVariable Long parking_id,
                                   @PathVariable Long zone_id) {
        double percentage = spaceService.getZoneOccupancyPercentage(zone_id);
        return percentage + "%";
    }

    @GetMapping("/{parking_id}/{zone_id}/available/{type}")
    public String isSpaceTypeAvailable(@PathVariable Long parking_id,
                                       @PathVariable Long zone_id,
                                       @PathVariable SpaceType type) {
        return spaceService.isSpaceTypeAvailable(parking_id, zone_id, type);
    }

    @GetMapping("/{parking_id}/{zone_id}/count")
    public int getZoneSpaceCount(@PathVariable Long parking_id,
                                 @PathVariable Long zone_id) {
        return spaceService.getZoneSpaceCount(parking_id, zone_id);
    }
}
