package com.nikko.parkingsystem.controller;

import com.nikko.parkingsystem.model.Parking;
import com.nikko.parkingsystem.model.ParkingSession;
import com.nikko.parkingsystem.service.PriceListService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pricing")
public class PriceListController {

    private final PriceListService priceListService;

    public PriceListController(PriceListService priceListService) {
        this.priceListService = priceListService;
    }

    @PostMapping("/calculate")
    public double calculatePrice(@RequestBody PricingPayload payload) {
        return priceListService.calculatePrice(payload.getSession(), payload.getParking());
    }

    @PostMapping("/discount")
    public double applyDiscount(@RequestParam double basePrice,
                                @RequestParam boolean weekend,
                                @RequestParam boolean specialSpace) {
        return priceListService.applyDiscounts(basePrice, weekend, specialSpace);
    }

    @Setter
    @Getter
    public static class PricingPayload {
        private ParkingSession session;
        private Parking parking;

    }
}

