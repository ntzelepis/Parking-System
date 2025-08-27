package com.nikko.parkingsystem.controller;

import com.nikko.parkingsystem.dto.PaymentReceiptDTO;
import com.nikko.parkingsystem.model.Payment;
import com.nikko.parkingsystem.model.Vehicle;
import com.nikko.parkingsystem.service.PaymentService;
import com.nikko.parkingsystem.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final VehicleService vehicleService;

    @GetMapping("/{vehicleId}")
    public List<Payment> getPayments(@PathVariable Long vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found for ID: " + vehicleId));
        return paymentService.getPaymentsByVehicle(vehicle);
    }

    @GetMapping("/{vehicleId}/total")
    public double getTotalPaid(@PathVariable Long vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found for ID: " + vehicleId));
        return paymentService.getTotalPaidByVehicle(vehicle);
    }

    @PostMapping("/receipt")
    public PaymentReceiptDTO generateReceipt(@RequestBody Payment payment) {
        return paymentService.generateReceiptDTO(payment);
    }

}
