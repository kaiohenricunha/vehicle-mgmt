package com.kcunha.vehicles_mgmt.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kcunha.vehicles_mgmt.models.Vehicle;

@Repository
public interface VehicleRepository extends ReactiveCrudRepository<Vehicle, Long> {
}
