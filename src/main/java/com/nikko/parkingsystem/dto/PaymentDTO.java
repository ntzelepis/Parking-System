package com.nikko.parkingsystem.dto;

import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;
    private double amount;
    private Long vehicleId;
    private Long sessionId;
    private Long parkingId;
    private Long parkingSpaceId;
}
