package com.nikko.parkingsystem.service;

import com.nikko.parkingsystem.model.Parking;
import com.nikko.parkingsystem.model.PricingCategory;
import com.nikko.parkingsystem.repo.PricingCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingCategoryService {

    private final PricingCategoryRepository pricingCategoryRepository;

    @Transactional(readOnly = true)
    public List<PricingCategory> getCategoriesForParking(Parking parking) {
        return pricingCategoryRepository.findByParking(parking);
    }

    @Transactional
    public PricingCategory saveCategory(PricingCategory category) {
        return pricingCategoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        pricingCategoryRepository.deleteById(categoryId);
    }

    @Transactional(readOnly = true)
    public boolean hasOverlappingCategories(Parking parking) {
        List<PricingCategory> categories = getCategoriesForParking(parking);
        categories.sort(Comparator.comparingDouble(PricingCategory::getMinDuration));
        for (int i = 1; i < categories.size(); i++) {
            if (categories.get(i).getMinDuration() < categories.get(i - 1).getMaxDuration()) {
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public boolean hasGapsInCoverage(Parking parking) {
        List<PricingCategory> categories = getCategoriesForParking(parking);
        categories.sort(Comparator.comparingDouble(PricingCategory::getMinDuration));
        for (int i = 1; i < categories.size(); i++) {
            if (categories.get(i).getMinDuration() > categories.get(i - 1).getMaxDuration()) {
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public boolean isValidCategoryStructure(Parking parking, PricingCategory newCategory) {
        List<PricingCategory> categories = getCategoriesForParking(parking);
        if (newCategory != null) categories.add(newCategory);

        categories.sort(Comparator.comparingDouble(PricingCategory::getMinDuration));

        for (int i = 1; i < categories.size(); i++) {
            double prevMax = categories.get(i - 1).getMaxDuration();
            double currMin = categories.get(i).getMinDuration();

            if (currMin < prevMax || currMin > prevMax) {
                return false;
            }
        }

        return true;
    }
}
