package com.nikko.parkingsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateParkingRequest {

    @NotBlank
    private String name;

    @Min(1)
    private int zoneCount;

    @NotNull
    private Long priceListId;
}
