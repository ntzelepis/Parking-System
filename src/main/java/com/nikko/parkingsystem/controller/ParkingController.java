package com.nikko.parkingsystem.controller;

import com.nikko.parkingsystem.dto.ParkingSummaryDTO;
import com.nikko.parkingsystem.service.ParkingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("/names")
    public List<ParkingSummaryDTO> getAllParkingSummaries() {
        return parkingService.getAllParkingSummaries();
    }


    @PostMapping("/create")
    public void createParking(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        int zoneCount = (Integer) request.get("zoneCount");
        Long priceListId = Long.valueOf((Integer) request.get("priceListId"));
        parkingService.createParkingWithZones(name, zoneCount, priceListId);
    }


}
