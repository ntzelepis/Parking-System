package com.nikko.parkingsystem.service;

import com.nikko.parkingsystem.dto.PaymentReceiptDTO;
import com.nikko.parkingsystem.model.*;
import com.nikko.parkingsystem.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ParkingService parkingService;
    private final PriceListService priceListService;

    @Transactional
    public Payment processPayment(ParkingSession session) {
        Parking parking = parkingService.getParkingByName(session.getParkingSpace().getParkingName());
        double amount = priceListService.calculatePrice(session, parking);

        Payment payment = new Payment(session.getVehicle(), session, amount);
        session.setPayment(payment);
        paymentRepository.save(payment);

        return payment;
    }

    public PaymentReceiptDTO generateReceiptDTO(Payment rawPayment) {
        Payment payment = paymentRepository.findBySessionWithDetails(rawPayment.getSession())
                .orElseThrow(() -> new IllegalStateException("Payment not found"));

        ParkingSession session = payment.getSession();
        Vehicle vehicle = payment.getVehicle();

        Hibernate.initialize(vehicle);
        Hibernate.initialize(session.getParkingSpace());

        PaymentReceiptDTO dto = new PaymentReceiptDTO();
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setModel(vehicle.getModel());
        dto.setType(vehicle.getType().name());
        dto.setZone(session.getParkingSpace().getZoneName());
        dto.setSpaceType(session.getParkingSpace().getType().name());
        dto.setEntryTime(session.getStart());
        dto.setExitTime(session.getEnd());
        dto.setDurationHours(session.getDurationHours());
        dto.setAmountPaid(payment.getAmount());

        return dto;
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
