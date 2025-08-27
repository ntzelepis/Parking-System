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
        String parkingName = (String) request.get("parkingName");
        String zoneLetter = (String) request.get("zoneLetter");
        int count = (Integer) request.get("count");
        String typeStr = (String) request.get("type");
        SpaceType type = SpaceType.valueOf(typeStr.toUpperCase());
        parkingService.addValidatedSpaceToZone(parkingName, zoneLetter, count, type);
    }

    @GetMapping("/{parkingName}/names")
    public List<String> getZoneNames(@PathVariable String parkingName) {
        return zoneService.getZoneNamesByParking(parkingName);
    }
}

