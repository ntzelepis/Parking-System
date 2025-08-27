package com.nikko.parkingsystem.controller;

import com.nikko.parkingsystem.model.Parking;
import com.nikko.parkingsystem.model.PricingCategory;
import com.nikko.parkingsystem.service.PricingCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class PricingCategoryController {

    private final PricingCategoryService categoryService;

    public PricingCategoryController(PricingCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{parkingName}")
    public List<PricingCategory> getCategories(@PathVariable String parkingName) {
        Parking parking = new Parking();
        parking.setName(parkingName);
        return categoryService.getCategoriesForParking(parking);
    }

    @PostMapping
    public PricingCategory saveCategory(@RequestBody PricingCategory category) {
        return categoryService.saveCategory(category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/{parkingName}/validate")
    public boolean validateStructure(@PathVariable String parkingName) {
        Parking parking = new Parking();
        parking.setName(parkingName);
        return categoryService.isValidCategoryStructure(parking, null); // You’ll need to pass a real category
    }
}

