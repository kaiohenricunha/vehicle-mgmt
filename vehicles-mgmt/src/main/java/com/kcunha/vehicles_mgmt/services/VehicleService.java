package com.kcunha.vehicles_mgmt.services;

import com.kcunha.vehicles_mgmt.models.Vehicle;
import com.kcunha.vehicles_mgmt.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public void deleteById(Long id) {
        vehicleRepository.deleteById(id);
    }
}
