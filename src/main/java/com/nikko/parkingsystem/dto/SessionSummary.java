package com.nikko.parkingsystem.dto;

public record SessionSummary(

        double durationHours,
        double amountPaid,
        String zoneName
) {}
