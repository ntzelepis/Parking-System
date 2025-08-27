package com.nikko.parkingsystem.repo;

import com.nikko.parkingsystem.model.PricingCategory;
import com.nikko.parkingsystem.model.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricingCategoryRepository extends JpaRepository<PricingCategory, Long> {
    List<PricingCategory> findByParking(Parking parking);
}

