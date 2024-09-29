package com.kcunha.vehicles_mgmt.controllers;

import com.kcunha.vehicles_mgmt.models.Vehicle;
import com.kcunha.vehicles_mgmt.services.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    public void testGetAllVehicles() throws Exception {
        Mockito.when(vehicleService.findAll()).thenReturn(Arrays.asList(vehicle1, vehicle2));

        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].make").value("Toyota"))
                .andExpect(jsonPath("$[0].model").value("Camry"))
                .andExpect(jsonPath("$[0].year").value(2020))
                .andExpect(jsonPath("$[1].make").value("Honda"))
                .andExpect(jsonPath("$[1].model").value("Civic"))
                .andExpect(jsonPath("$[1].year").value(2019));
    }

    @Test
    public void testGetVehicleById() throws Exception {
        Mockito.when(vehicleService.findById(1L)).thenReturn(Optional.of(vehicle1));

        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Camry"))
                .andExpect(jsonPath("$.year").value(2020));
    }

    @Test
    public void testGetVehicleById_NotFound() throws Exception {
        Mockito.when(vehicleService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateVehicle() throws Exception {
        Vehicle newVehicle = new Vehicle("Ford", "Mustang", 2021);
        newVehicle.setId(3L);
        
        Mockito.when(vehicleService.save(any(Vehicle.class))).thenReturn(newVehicle);

        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"make\":\"Ford\",\"model\":\"Mustang\",\"year\":2021}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.make").value("Ford"))
                .andExpect(jsonPath("$.model").value("Mustang"))
                .andExpect(jsonPath("$.year").value(2021));
    }

    @Test
    public void testUpdateVehicle() throws Exception {
        Mockito.when(vehicleService.findById(1L)).thenReturn(Optional.of(vehicle1));
        vehicle1.setMake("Tesla");

        Mockito.when(vehicleService.save(any(Vehicle.class))).thenReturn(vehicle1);

        mockMvc.perform(put("/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"make\":\"Tesla\",\"model\":\"Model S\",\"year\":2020}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Tesla"))
                .andExpect(jsonPath("$.model").value("Model S"))
                .andExpect(jsonPath("$.year").value(2020));
    }

    @Test
    public void testUpdateVehicle_NotFound() throws Exception {
        Mockito.when(vehicleService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"make\":\"Tesla\",\"model\":\"Model S\",\"year\":2020}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteVehicle() throws Exception {
        Mockito.when(vehicleService.findById(1L)).thenReturn(Optional.of(vehicle1));

        mockMvc.perform(delete("/vehicles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteVehicle_NotFound() throws Exception {
        Mockito.when(vehicleService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/vehicles/1"))
                .andExpect(status().isNotFound());
    }
}
