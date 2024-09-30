package com.kcunha.vehicles_mgmt.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.kcunha.vehicles_mgmt.models.Vehicle;
import com.kcunha.vehicles_mgmt.services.VehicleService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(VehicleController.class)
public class VehicleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private VehicleService vehicleService;

    private Vehicle vehicle1;
    private Vehicle vehicle2;

    @BeforeEach
    void setUp() {
        vehicle1 = new Vehicle("Toyota", "Camry", 2020);
        vehicle1.setId(1L);
        
        vehicle2 = new Vehicle("Honda", "Civic", 2019);
        vehicle2.setId(2L);
    }    

    @Test
    public void testGetAllVehicles() {
        Mockito.when(vehicleService.findAll()).thenReturn(Flux.just(vehicle1, vehicle2));

        webTestClient.get()
                .uri("/vehicles")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBodyList(Vehicle.class)
                .consumeWith(response -> {
                    var vehicles = response.getResponseBody();
                    assert vehicles != null;
                    assert vehicles.size() == 2;
                    assert vehicles.contains(vehicle1);
                    assert vehicles.contains(vehicle2);
                });
    }

    @Test
    public void testGetVehicleById() {
        Mockito.when(vehicleService.findById(1L)).thenReturn(Mono.just(vehicle1));

        webTestClient.get()
                .uri("/vehicles/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody(Vehicle.class)
                .consumeWith(response -> {
                    Vehicle responseBody = response.getResponseBody();
                    assert responseBody != null;
                    assert responseBody.equals(vehicle1);
                });
    }

    @Test
    public void testGetVehicleById_NotFound() {
        Mockito.when(vehicleService.findById(1L)).thenReturn(Mono.empty());
    
        webTestClient.get()
                .uri("/vehicles/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }    

    @SuppressWarnings("null")
    @Test
    public void testCreateVehicle() {
        Vehicle newVehicle = new Vehicle("Ford", "Mustang", 2021);
        newVehicle.setId(3L);
    
        Mockito.when(vehicleService.save(any(Vehicle.class))).thenReturn(Mono.just(newVehicle));
    
        webTestClient.post()
                .uri("/vehicles")
                .contentType(APPLICATION_JSON)
                .bodyValue(newVehicle)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody(Vehicle.class)
                .consumeWith(response -> {
                    Vehicle responseBody = response.getResponseBody();
                    assert responseBody != null;
                    assert responseBody.getId().equals(3L);
                    assert responseBody.getMake().equals("Ford");
                    assert responseBody.getModel().equals("Mustang");
                    assert responseBody.getYear() == 2021;
                });
    }    

    @Test
    public void testDeleteVehicle() {
        Mockito.when(vehicleService.findById(1L)).thenReturn(Mono.just(vehicle1));
        Mockito.when(vehicleService.deleteById(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/vehicles/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void testDeleteVehicle_NotFound() {
        Mockito.when(vehicleService.findById(1L)).thenReturn(Mono.empty());
    
        webTestClient.delete()
                .uri("/vehicles/1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }    
}
