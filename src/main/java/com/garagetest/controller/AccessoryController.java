package com.garagetest.controller;

import com.garagetest.dto.AccessoryDTO;
import com.garagetest.service.AccessoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accessories")
@RequiredArgsConstructor
public class AccessoryController {
    
    private final AccessoryService accessoryService;
    
    @PostMapping
    public ResponseEntity<AccessoryDTO> createAccessory(@Valid @RequestBody AccessoryDTO accessoryDTO) {
        AccessoryDTO createdAccessory = accessoryService.createAccessory(accessoryDTO);
        return new ResponseEntity<>(createdAccessory, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AccessoryDTO> getAccessoryById(@PathVariable Long id) {
        AccessoryDTO accessory = accessoryService.getAccessoryById(id);
        return ResponseEntity.ok(accessory);
    }
    
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<AccessoryDTO>> getAccessoriesByVehicleId(@PathVariable Long vehicleId) {
        List<AccessoryDTO> accessories = accessoryService.getAccessoriesByVehicleId(vehicleId);
        return ResponseEntity.ok(accessories);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AccessoryDTO>> getAccessoriesByType(@PathVariable String type) {
        List<AccessoryDTO> accessories = accessoryService.getAccessoriesByType(type);
        return ResponseEntity.ok(accessories);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AccessoryDTO> updateAccessory(
            @PathVariable Long id,
            @Valid @RequestBody AccessoryDTO accessoryDTO) {
        AccessoryDTO updatedAccessory = accessoryService.updateAccessory(id, accessoryDTO);
        return ResponseEntity.ok(updatedAccessory);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessory(@PathVariable Long id) {
        accessoryService.deleteAccessory(id);
        return ResponseEntity.noContent().build();
    }
}