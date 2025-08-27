package com.nikko.parkingsystem.service;

import com.nikko.parkingsystem.model.Parking;
import com.nikko.parkingsystem.model.PricingCategory;
import com.nikko.parkingsystem.repo.PricingCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class PricingCategoryService {

    private final PricingCategoryRepository pricingCategoryRepository;

    @Autowired
    public PricingCategoryService(PricingCategoryRepository pricingCategoryRepository) {
        this.pricingCategoryRepository = pricingCategoryRepository;
    }

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
            double prevMax = categories.get(i - 1).getMaxDuration();
            double currMin = categories.get(i).getMinDuration();
            if (currMin < prevMax) return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public boolean hasGapsInCoverage(Parking parking) {
        List<PricingCategory> categories = getCategoriesForParking(parking);
        categories.sort(Comparator.comparingDouble(PricingCategory::getMinDuration));
        for (int i = 1; i < categories.size(); i++) {
            double prevMax = categories.get(i - 1).getMaxDuration();
            double currMin = categories.get(i).getMinDuration();
            if (currMin > prevMax) return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public boolean isValidCategoryStructure(Parking parking, PricingCategory newCategory) {
        List<PricingCategory> categories = getCategoriesForParking(parking);
        categories.add(newCategory);

        categories.sort(Comparator.comparingDouble(PricingCategory::getMinDuration));

        for (int i = 1; i < categories.size(); i++) {
            double prevMax = categories.get(i - 1).getMaxDuration();
            double currMin = categories.get(i).getMinDuration();

            if (currMin < prevMax) {
                System.out.printf("Overlap detected: %.2f < %.2f%n", currMin, prevMax);
                return false;
            }

            if (currMin > prevMax) {
                System.out.printf("Gap detected: %.2f > %.2f%n", currMin, prevMax);
                return false;
            }
        }

        return true;
    }

}
