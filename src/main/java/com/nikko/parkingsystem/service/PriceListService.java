package com.nikko.parkingsystem.service;

import com.nikko.parkingsystem.model.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class PriceListService {

    public double calculatePrice(ParkingSession session, Parking parking) {
        double hours = session.getDurationHours();
        if (hours <= 0) return 0.0;

        double total = 0.0;
        PriceList priceList = parking.getPriceList();
        List<PricingCategory> categories = parking.getPricingCategories();

        for (PricingCategory category : categories) {
            if (hours > category.getMinDuration()) {
                double applicableHours = Math.min(hours, category.getMaxDuration()) - category.getMinDuration();
                total += applicableHours * category.getBillingScale();
            }
        }

        if (total == 0.0) {
            total = priceList.getBaseRate() * hours;
        }

        DayOfWeek day = session.getStart().getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            total *= 0.8;
        }

        SpaceType type = session.getParkingSpace().getType();
        if (type == SpaceType.HANDICAPPED || type == SpaceType.PREGNANT) {
            total *= 0.5;
        }

        return Math.max(0.5, Math.round(total * 100.0) / 100.0); // Minimum charge
    }

    public double applyDiscounts(double basePrice, boolean isWeekend, boolean isSpecialSpace) {
        double discounted = basePrice;
        if (isWeekend) discounted *= 0.8;
        if (isSpecialSpace) discounted *= 0.5;
        return Math.max(0.5, Math.round(discounted * 100.0) / 100.0);
    }




}
