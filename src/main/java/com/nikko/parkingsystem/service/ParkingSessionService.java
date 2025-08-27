package com.nikko.parkingsystem.service;

import com.nikko.parkingsystem.model.*;
import com.nikko.parkingsystem.dto.SessionSummary;
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
                .sorted(Comparator.comparing(ParkingZone::getId))
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
    public Payment unparkVehicle(Long vehicleId) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (vehicleOpt.isEmpty()) return null;

        Vehicle vehicle = vehicleOpt.get();
        ParkingSession session = sessionRepository.findActiveSession(vehicle).orElse(null);
        if (session == null) return null;

        ParkingSpace space = session.getParkingSpace();
        Parking parking = parkingRepository.findByName(space.getParkingName()).orElse(null);
        if (parking == null) return null;

        session.setEnd(LocalDateTime.now());
        space.setOccupied(false);

        sessionRepository.save(session);
        parkingSpaceRepository.save(space);

        return paymentService.processPayment(session);
    }


    @Transactional(readOnly = true)
    public List<ParkingSession> getSessionsByVehicle(Vehicle vehicle) {
        return sessionRepository.findByVehicle(vehicle);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getSessionAnalytics(Vehicle vehicle) {
        List<ParkingSession> sessions = sessionRepository.findSessionsWithDetails(vehicle);

        List<SessionSummary> summaries = sessions.stream()
                .map(s -> new SessionSummary(
                        s.getDurationHours(),
                        s.getPayment() != null ? s.getPayment().getAmount() : 0.0,
                        s.getParkingSpace() != null ? s.getParkingSpace().getZoneName() : null
                ))
                .toList();

        double totalHours = summaries.stream().mapToDouble(SessionSummary::durationHours).sum();
        double average = summaries.isEmpty() ? 0 : totalHours / summaries.size();
        double totalPaid = summaries.stream().mapToDouble(SessionSummary::amountPaid).sum();
        double maxDuration = summaries.stream().mapToDouble(SessionSummary::durationHours).max().orElse(0);
        double minDuration = summaries.stream().mapToDouble(SessionSummary::durationHours).min().orElse(0);
        String mostUsedZone = summaries.stream()
                .map(SessionSummary::zoneName)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(z -> z, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        Map<String, Object> analytics = new LinkedHashMap<>();
        analytics.put("sessionCount", summaries.size());
        analytics.put("totalHours", totalHours);
        analytics.put("averageDuration", average);
        analytics.put("totalPaid", totalPaid);
        analytics.put("maxDuration", maxDuration);
        analytics.put("minDuration", minDuration);
        analytics.put("mostUsedZone", mostUsedZone);

        return analytics;
    }
}
