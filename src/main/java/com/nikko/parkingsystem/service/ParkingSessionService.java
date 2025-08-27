package com.nikko.parkingsystem.service;


import com.nikko.parkingsystem.model.*;
import com.nikko.parkingsystem.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParkingSessionService {

    private final ParkingSessionRepository sessionRepository;
    private final ParkingRepository parkingRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final PaymentService paymentService;
    private final ParkingZoneRepository zoneRepository;
    private final ParkingSpaceService parkingSpaceService;
    private final VehicleRepository vehicleRepository;

    public ParkingSessionService(ParkingSessionRepository sessionRepository,
                                 ParkingRepository parkingRepository,
                                 ParkingSpaceRepository parkingSpaceRepository,
                                 PaymentService paymentService,
                                 ParkingZoneRepository zoneRepository,
                                 ParkingSpaceService parkingSpaceService,
                                 VehicleRepository vehicleRepository) {
        this.sessionRepository = sessionRepository;
        this.parkingRepository = parkingRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.paymentService = paymentService;
        this.zoneRepository = zoneRepository;
        this.parkingSpaceService = parkingSpaceService;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public Optional<ParkingSpace> autoParkVehicle(String parkingName, SpaceType spaceType, Vehicle vehicle) {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        List<ParkingZone> zones = zoneRepository.findByParkingName(parkingName).stream()
                .sorted(Comparator.comparing(ParkingZone::getId)) // or use getName() if you prefer alphabetical
                .collect(Collectors.toList());

        for (ParkingZone zone : zones) {
            double occupancy = parkingSpaceService.getZoneOccupancyPercentage(zone.getId());
            if (occupancy >= 100.0) continue;

            List<ParkingSpace> availableSpaces = parkingSpaceRepository.findByZoneId(zone.getId()).stream()
                    .filter(space -> spaceType.equals(space.getType()) && !space.isOccupied())
                    .sorted(Comparator.comparing(ParkingSpace::getSpaceCode))
                    .collect(Collectors.toList());

            if (!availableSpaces.isEmpty()) {
                ParkingSpace spaceToPark = availableSpaces.get(0);
                spaceToPark.setOccupied(true);
                spaceToPark.setVehicle(savedVehicle);
                spaceToPark.setZone(zone);
                parkingSpaceRepository.saveAndFlush(spaceToPark);

                ParkingSession session = new ParkingSession();
                session.setVehicle(savedVehicle);
                session.setParkingSpace(spaceToPark);
                session.setStart(LocalDateTime.now());

                sessionRepository.saveAndFlush(session);
                return Optional.of(spaceToPark);
            }
        }

        return Optional.empty();
    }


    @Transactional
    public Payment unparkVehicle(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate);
        if (vehicle == null) return null;

        ParkingSession session = sessionRepository.findActiveSession(vehicle).orElse(null);
        if (session == null) return null;

        ParkingSpace space = session.getParkingSpace();
        Parking parking = parkingRepository.findByName(space.getParkingName()).orElse(null);
        if (parking == null) return null;

        LocalDateTime end = LocalDateTime.now();
        session.setEnd(end);
        space.setOccupied(false);

        sessionRepository.save(session);
        parkingSpaceRepository.save(space);

        Payment payment = paymentService.processPayment(session);
        System.out.printf("Payment recorded: $%.2f for session ID: %d%n", payment.getAmount(), session.getId());
        return payment;
    }


    @Transactional(readOnly = true)
    public List<ParkingSession> getSessionsByVehicle(Vehicle vehicle) {
        return sessionRepository.findByVehicle(vehicle);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getSessionAnalytics(Vehicle vehicle) {
        List<ParkingSession> sessions = getSessionsByVehicle(vehicle);

        double totalHours = sessions.stream().mapToDouble(ParkingSession::getDurationHours).sum();
        double average = sessions.isEmpty() ? 0 : totalHours / sessions.size();
        double totalPaid = sessions.stream()
                .map(ParkingSession::getPayment)
                .filter(Objects::nonNull)
                .mapToDouble(Payment::getAmount)
                .sum();
        double maxDuration = sessions.stream().mapToDouble(ParkingSession::getDurationHours).max().orElse(0);
        double minDuration = sessions.stream().mapToDouble(ParkingSession::getDurationHours).min().orElse(0);
        String mostUsedZone = sessions.stream()
                .map(s -> s.getParkingSpace().getZoneName())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(z -> z, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalHours", totalHours);
        analytics.put("averageDuration", average);
        analytics.put("totalPaid", totalPaid);
        analytics.put("sessionCount", sessions.size());
        analytics.put("maxDuration", maxDuration);
        analytics.put("minDuration", minDuration);
        analytics.put("mostUsedZone", mostUsedZone);
        return analytics;
    }
}