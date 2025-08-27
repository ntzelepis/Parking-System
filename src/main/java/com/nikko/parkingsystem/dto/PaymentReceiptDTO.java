package com.nikko.parkingsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentReceiptDTO {
    private String licensePlate;
    private String model;
    private String type;
    private String zone;
    private String spaceType;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double durationHours;
    private double amountPaid;
}
