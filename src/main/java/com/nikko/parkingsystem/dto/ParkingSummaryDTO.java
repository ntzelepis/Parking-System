package com.nikko.parkingsystem.dto;

import lombok.Data;

@Data
public class ParkingSummaryDTO {
    private Long id;
    private String name;
    private int zoneCount;
}
