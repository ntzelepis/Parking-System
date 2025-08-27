package com.nikko.parkingsystem.controller;

import com.nikko.parkingsystem.model.SpaceType;
import com.nikko.parkingsystem.service.ParkingService;
import com.nikko.parkingsystem.service.ParkingZoneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/zones")
public class ParkingZoneController {

    private final ParkingZoneService zoneService;
    private final ParkingService parkingService;

    public ParkingZoneController(ParkingZoneService zoneService, ParkingService parkingService) {
        this.zoneService = zoneService;
        this.parkingService = parkingService;
    }

    @PostMapping("/spaces/add")
    public void addSpaces(@RequestBody Map<String, Object> request) {
        Long parkingId = Long.valueOf((Integer) request.get("parking_id"));
        Long zoneId = Long.valueOf((Integer) request.get("zone_id"));
        int count = (Integer) request.get("count");
        String typeStr = (String) request.get("type");
        SpaceType type = SpaceType.valueOf(typeStr.toUpperCase());
        parkingService.addValidatedSpaceToZone(parkingId, zoneId, count, type);
    }

    @GetMapping("/{parking_id}/names")
    public List<String> getZoneNames(@PathVariable Long parking_id) {
        return zoneService.getZoneNamesByParkingId(parking_id);
    }
}

