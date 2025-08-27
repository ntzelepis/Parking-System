package com.nikko.parkingsystem.service;

import com.nikko.parkingsystem.model.Payment;
import com.nikko.parkingsystem.model.ParkingSession;
import com.nikko.parkingsystem.model.Vehicle;
import com.nikko.parkingsystem.repo.PaymentRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ParkingService parkingService;
    private final PriceListService priceListService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          ParkingService parkingService,
                          PriceListService priceListService) {
        this.paymentRepository = paymentRepository;
        this.parkingService = parkingService;
        this.priceListService = priceListService;
    }

    @Transactional
    public Payment processPayment(ParkingSession session) {
        var parking = parkingService.getParkingByName(session.getParkingSpace().getParkingName());
        double amount = priceListService.calculatePrice(session, parking);

        Payment payment = new Payment(session.getVehicle(), session, amount);
        session.setPayment(payment);
        paymentRepository.save(payment);

        System.out.printf("Payment recorded: $%.2f for session ID: %d%n", amount, session.getId());
        return payment;
    }

    @Transactional(readOnly = true)
    public String generateReceipt(Payment rawPayment) {
        Payment payment = paymentRepository.findBySessionWithDetails(rawPayment.getSession())
                .orElseThrow(() -> new IllegalStateException("Payment not found"));

        ParkingSession session = payment.getSession();
        Vehicle vehicle = payment.getVehicle();

        Hibernate.initialize(vehicle);
        Hibernate.initialize(session.getParkingSpace());

        return String.format("""
                === Parking Receipt ===
                License Plate: %s
                Model: %s
                Type: %s
                Zone: %s
                Space Type: %s
                Entry Time: %s
                Exit Time: %s
                Duration: %.2f hours
                Amount Paid: $%.2f
                """,
                vehicle.getLicensePlate(),
                vehicle.getModel(),
                vehicle.getType(),
                session.getParkingSpace().getZoneName(),
                session.getParkingSpace().getType(),
                session.getStart(),
                session.getEnd(),
                session.getDurationHours(),
                payment.getAmount()
        );
    }

    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByVehicle(Vehicle vehicle) {
        return paymentRepository.findByVehicle(vehicle);
    }

    @Transactional(readOnly = true)
    public double getTotalPaidByVehicle(Vehicle vehicle) {
        return getPaymentsByVehicle(vehicle).stream()
                .map(Payment::getAmount)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
