package com.kcunha.vehicles_mgmt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kcunha.vehicles_mgmt.models.Vehicle;
import com.kcunha.vehicles_mgmt.services.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Vehicle Management", description = "API for managing vehicles")
@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Operation(summary = "Retrieve all vehicles", description = "Get a list of all available vehicles")
    @GetMapping
    public Flux<Vehicle> getAllVehicles() {
        return vehicleService.findAll();
    }

    @Operation(summary = "Retrieve a vehicle by ID", description = "Get the details of a specific vehicle by ID",
            responses = {
                @ApiResponse(responseCode = "200", description = "Vehicle found",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = Vehicle.class))),
                @ApiResponse(responseCode = "404", description = "Vehicle not found")
            })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Vehicle>> getVehicleById(@PathVariable Long id) {
        return vehicleService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new vehicle", description = "Add a new vehicle to the catalog",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Vehicle details",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"make\": \"Ford\", \"model\": \"Mustang\", \"year\": 2021}")
                    )
            ),
            responses = {
                @ApiResponse(responseCode = "201", description = "Vehicle created",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = Vehicle.class)))
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.save(vehicle);
    }

    @Operation(summary = "Delete a vehicle by ID", description = "Remove a specific vehicle from the catalog",
            responses = {
                @ApiResponse(responseCode = "204", description = "Vehicle deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Vehicle not found")
            })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteVehicle(@PathVariable Long id) {
        return vehicleService.findById(id)
                .flatMap(vehicle -> vehicleService.deleteById(vehicle.getId())
                        .then(Mono.just(ResponseEntity.noContent().<Void>build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
