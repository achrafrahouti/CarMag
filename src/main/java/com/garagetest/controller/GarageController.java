package com.garagetest.controller;

import com.garagetest.dto.GarageDTO;
import com.garagetest.service.GarageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
public class GarageController {
    
    private final GarageService garageService;
    
    @PostMapping
    public ResponseEntity<GarageDTO> createGarage(@Valid @RequestBody GarageDTO garageDTO) {
        GarageDTO createdGarage = garageService.createGarage(garageDTO);
        return new ResponseEntity<>(createdGarage, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GarageDTO> getGarageById(@PathVariable Long id) {
        GarageDTO garage = garageService.getGarageById(id);
        return ResponseEntity.ok(garage);
    }
    
    @GetMapping
    public ResponseEntity<Page<GarageDTO>> getAllGarages(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<GarageDTO> garages = garageService.getAllGarages(pageable);
        return ResponseEntity.ok(garages);
    }
    
    @GetMapping("/search/name")
    public ResponseEntity<Page<GarageDTO>> getGaragesByName(
            @RequestParam String name,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<GarageDTO> garages = garageService.getGaragesByName(name, pageable);
        return ResponseEntity.ok(garages);
    }
    
    @GetMapping("/search/city")
    public ResponseEntity<Page<GarageDTO>> getGaragesByCity(
            @RequestParam String city,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<GarageDTO> garages = garageService.getGaragesByCity(city, pageable);
        return ResponseEntity.ok(garages);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GarageDTO> updateGarage(
            @PathVariable Long id,
            @Valid @RequestBody GarageDTO garageDTO) {
        GarageDTO updatedGarage = garageService.updateGarage(id, garageDTO);
        return ResponseEntity.ok(updatedGarage);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarage(@PathVariable Long id) {
        garageService.deleteGarage(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search/vehicle-brand")
    public ResponseEntity<List<GarageDTO>> getGaragesByVehicleBrand(@RequestParam String brand) {
        List<GarageDTO> garages = garageService.getGaragesByVehicleBrand(brand);
        return ResponseEntity.ok(garages);
    }
    
    @GetMapping("/search/vehicle-model")
    public ResponseEntity<List<GarageDTO>> getGaragesByVehicleModel(@RequestParam String model) {
        List<GarageDTO> garages = garageService.getGaragesByVehicleModel(model);
        return ResponseEntity.ok(garages);
    }
    
    @GetMapping("/search/accessory-type")
    public ResponseEntity<List<GarageDTO>> getGaragesByAccessoryType(@RequestParam String type) {
        List<GarageDTO> garages = garageService.getGaragesByAccessoryType(type);
        return ResponseEntity.ok(garages);
    }
}