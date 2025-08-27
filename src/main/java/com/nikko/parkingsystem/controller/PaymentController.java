package com.nikko.parkingsystem.controller;

import com.nikko.parkingsystem.model.Payment;
import com.nikko.parkingsystem.model.Vehicle;
import com.nikko.parkingsystem.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{licensePlate}")
    public List<Payment> getPayments(@PathVariable String licensePlate) {
        Vehicle vehicle = new Vehicle(licensePlate);
        return paymentService.getPaymentsByVehicle(vehicle);
    }

    @GetMapping("/{licensePlate}/total")
    public double getTotalPaid(@PathVariable String licensePlate) {
        Vehicle vehicle = new Vehicle(licensePlate);
        return paymentService.getTotalPaidByVehicle(vehicle);
    }

    @PostMapping("/receipt")
    public String generateReceipt(@RequestBody Payment payment) {
        return paymentService.generateReceipt(payment);
    }
}
