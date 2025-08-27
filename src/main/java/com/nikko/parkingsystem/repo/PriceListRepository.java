package com.nikko.parkingsystem.repo;

import com.nikko.parkingsystem.model.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceListRepository extends JpaRepository<PriceList, Long> {
}
