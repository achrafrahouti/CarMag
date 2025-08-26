package com.garagetest.service;

import com.garagetest.dto.AccessoryDTO;
import com.garagetest.model.Accessory;
import com.garagetest.model.Vehicle;
import com.garagetest.repository.AccessoryRepository;
import com.garagetest.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccessoryService {
    
    private final AccessoryRepository accessoryRepository;
    private final VehicleRepository vehicleRepository;
    
    public AccessoryDTO createAccessory(AccessoryDTO accessoryDTO) {
        Vehicle vehicle = vehicleRepository.findById(accessoryDTO.getVehicleId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + accessoryDTO.getVehicleId()));
        
        Accessory accessory = mapToEntity(accessoryDTO);
        accessory.setVehicle(vehicle);
        
        Accessory savedAccessory = accessoryRepository.save(accessory);
        return mapToDTO(savedAccessory);
    }
    
    @Transactional(readOnly = true)
    public AccessoryDTO getAccessoryById(Long id) {
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Accessory not found with id: " + id));
        return mapToDTO(accessory);
    }
    
    @Transactional(readOnly = true)
    public List<AccessoryDTO> getAccessoriesByVehicleId(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new EntityNotFoundException("Vehicle not found with id: " + vehicleId);
        }
        
        return accessoryRepository.findByVehicleId(vehicleId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AccessoryDTO> getAccessoriesByType(String type) {
        return accessoryRepository.findByTypeIgnoreCase(type).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public AccessoryDTO updateAccessory(Long id, AccessoryDTO accessoryDTO) {
        Accessory existingAccessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Accessory not found with id: " + id));
        
        // If vehicle is being changed
        if (!existingAccessory.getVehicle().getId().equals(accessoryDTO.getVehicleId())) {
            Vehicle newVehicle = vehicleRepository.findById(accessoryDTO.getVehicleId())
                    .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + accessoryDTO.getVehicleId()));
            
            existingAccessory.setVehicle(newVehicle);
        }
        
        existingAccessory.setName(accessoryDTO.getName());
        existingAccessory.setDescription(accessoryDTO.getDescription());
        existingAccessory.setPrice(accessoryDTO.getPrice());
        existingAccessory.setType(accessoryDTO.getType());
        
        Accessory updatedAccessory = accessoryRepository.save(existingAccessory);
        return mapToDTO(updatedAccessory);
    }
    
    public void deleteAccessory(Long id) {
        if (!accessoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Accessory not found with id: " + id);
        }
        accessoryRepository.deleteById(id);
    }
    
    // Helper methods for mapping between DTO and entity
    private AccessoryDTO mapToDTO(Accessory accessory) {
        AccessoryDTO dto = new AccessoryDTO();
        dto.setId(accessory.getId());
        dto.setName(accessory.getName());
        dto.setDescription(accessory.getDescription());
        dto.setPrice(accessory.getPrice());
        dto.setType(accessory.getType());
        dto.setVehicleId(accessory.getVehicle().getId());
        return dto;
    }
    
    private Accessory mapToEntity(AccessoryDTO dto) {
        Accessory accessory = new Accessory();
        accessory.setId(dto.getId());
        accessory.setName(dto.getName());
        accessory.setDescription(dto.getDescription());
        accessory.setPrice(dto.getPrice());
        accessory.setType(dto.getType());
        // Vehicle is set separately
        return accessory;
    }
}