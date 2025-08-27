package com.nikko.parkingsystem.service;

import com.nikko.parkingsystem.model.ParkingSession;
import com.nikko.parkingsystem.model.Vehicle;
import com.nikko.parkingsystem.model.VehicleType;
import com.nikko.parkingsystem.repo.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle findVehicle(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate);
    }

    public Vehicle createVehicle(String licensePlate, String model, VehicleType type) {
        String resolvedModel = (model == null || model.isBlank()) ? "Unknown" : model;
        Vehicle vehicle = new Vehicle(resolvedModel, licensePlate, type);
        return vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicle(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vehicle getVehicleWithSessions(String licensePlate) {
        return vehicleRepository.findByLicensePlateWithSessions(licensePlate)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ParkingSession> getSessionsByLicensePlate(String licensePlate) {
        Vehicle vehicle = getVehicleWithSessions(licensePlate);
        return (vehicle != null) ? List.copyOf(vehicle.getSessions()) : List.of();
    }

    @Transactional(readOnly = true)
    public ParkingSession getLatestSession(String licensePlate) {
        Vehicle vehicle = getVehicleWithSessions(licensePlate);
        if (vehicle == null || vehicle.getSessions().isEmpty()) {
            return null;
        }
        return vehicle.getSessions().stream()
                .sorted(Comparator.comparing(ParkingSession::getStart).reversed())
                .findFirst()
                .orElse(null);
    }

    public Optional<Vehicle> findById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

}
