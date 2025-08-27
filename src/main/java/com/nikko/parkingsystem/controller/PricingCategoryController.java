package com.nikko.parkingsystem.controller;

import com.nikko.parkingsystem.model.Parking;
import com.nikko.parkingsystem.model.PricingCategory;
import com.nikko.parkingsystem.dto.CreateParkingRequest;
import com.nikko.parkingsystem.service.PricingCategoryService;
import com.nikko.parkingsystem.service.ParkingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class PricingCategoryController {

    private final PricingCategoryService categoryService;
    private final ParkingService parkingService;

    @PostMapping("/create")
    public void createParking(@RequestBody @Valid CreateParkingRequest request) {
        parkingService.createParkingWithZones(
                request.getName(),
                request.getZoneCount(),
                request.getPriceListId()
        );
    }

    @GetMapping("/{parkingId}")
    public List<PricingCategory> getCategories(@PathVariable Long parkingId) {
        Parking parking = parkingService.getParkingById(parkingId);
        return categoryService.getCategoriesForParking(parking);
    }

    @PostMapping
    public PricingCategory saveCategory(@RequestBody PricingCategory category) {
        return categoryService.saveCategory(category);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/{parkingId}/validate")
    public boolean validateStructure(@PathVariable Long parkingId) {
        Parking parking = parkingService.getParkingById(parkingId);
        return categoryService.isValidCategoryStructure(parking, null);
    }

}
