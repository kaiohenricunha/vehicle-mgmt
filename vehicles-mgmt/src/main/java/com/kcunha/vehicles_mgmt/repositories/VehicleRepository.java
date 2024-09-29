package com.kcunha.vehicles_mgmt.repositories;

import com.kcunha.vehicles_mgmt.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
