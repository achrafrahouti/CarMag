package com.garagetest.controller;

import com.garagetest.dto.VehicleDTO;
import com.garagetest.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    
    private final VehicleService vehicleService;
    
    @PostMapping
    public ResponseEntity<VehicleDTO> createVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO createdVehicle = vehicleService.createVehicle(vehicleDTO);
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }
    
    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByGarageId(@PathVariable Long garageId) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByGarageId(garageId);
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/model/{model}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByModel(@PathVariable String model) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByModel(model);
        return ResponseEntity.ok(vehicles);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);
        return ResponseEntity.ok(updatedVehicle);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}