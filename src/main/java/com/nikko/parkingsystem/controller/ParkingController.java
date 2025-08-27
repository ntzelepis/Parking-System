package com.nikko.parkingsystem.controller;

import com.nikko.parkingsystem.service.ParkingService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("/names")
    public java.util.List<Map<String, Object>> getAllParkingSummaries() {
        return parkingService.getAllParkingSummaries();
    }

    @PostMapping("/create")
    public void createParking(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        int zoneCount = (Integer) request.get("zoneCount");
        parkingService.createParkingWithZones(name, zoneCount);
    }
}
