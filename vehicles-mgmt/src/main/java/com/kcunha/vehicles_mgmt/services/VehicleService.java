package com.kcunha.vehicles_mgmt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcunha.vehicles_mgmt.models.Vehicle;
import com.kcunha.vehicles_mgmt.repositories.VehicleRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Flux<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Mono<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Mono<Vehicle> save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Mono<Void> deleteById(Long id) {
        return vehicleRepository.deleteById(id);
    }
}
