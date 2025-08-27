package com.garagetest.service;

import com.garagetest.dto.VehicleDTO;
import com.garagetest.messaging.VehiclePublisher;
import com.garagetest.model.Garage;
import com.garagetest.model.Vehicle;
import com.garagetest.repository.GarageRepository;
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
public class VehicleService {
    
    private final VehicleRepository vehicleRepository;
    private final GarageRepository garageRepository;
    private final VehiclePublisher vehiclePublisher;
    
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        Garage garage = garageRepository.findById(vehicleDTO.getGarageId())
                .orElseThrow(() -> new EntityNotFoundException("Garage not found with id: " + vehicleDTO.getGarageId()));
        
        // Check if garage can add more vehicles (max 50)
        if (!garage.canAddVehicle()) {
            throw new IllegalStateException("Garage has reached maximum capacity of 50 vehicles");
        }
        
        Vehicle vehicle = mapToEntity(vehicleDTO);
        vehicle.setGarage(garage);
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        VehicleDTO savedVehicleDTO = mapToDTO(savedVehicle);
        
        // Publish vehicle created event
        vehiclePublisher.publishVehicleCreated(savedVehicleDTO);
        
        return savedVehicleDTO;
    }
    
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + id));
        return mapToDTO(vehicle);
    }
    
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByGarageId(Long garageId) {
        if (!garageRepository.existsById(garageId)) {
            throw new EntityNotFoundException("Garage not found with id: " + garageId);
        }
        
        return vehicleRepository.findByGarageId(garageId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByModel(String model) {
        return vehicleRepository.findAllByModel(model).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + id));
        
        // If garage is being changed, check if new garage can accept more vehicles
        if (!existingVehicle.getGarage().getId().equals(vehicleDTO.getGarageId())) {
            Garage newGarage = garageRepository.findById(vehicleDTO.getGarageId())
                    .orElseThrow(() -> new EntityNotFoundException("Garage not found with id: " + vehicleDTO.getGarageId()));
            
            if (!newGarage.canAddVehicle()) {
                throw new IllegalStateException("New garage has reached maximum capacity of 50 vehicles");
            }
            
            existingVehicle.setGarage(newGarage);
        }
        
        existingVehicle.setBrand(vehicleDTO.getBrand());
        existingVehicle.setModel(vehicleDTO.getModel());
        existingVehicle.setManufacturingYear(vehicleDTO.getManufacturingYear());
        existingVehicle.setFuelType(vehicleDTO.getFuelType());
        
        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        VehicleDTO updatedVehicleDTO = mapToDTO(updatedVehicle);
        
        // Publish vehicle updated event
        vehiclePublisher.publishVehicleUpdated(updatedVehicleDTO);
        
        return updatedVehicleDTO;
    }
    
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + id));
        
        // Get the DTO before deleting for the event
        VehicleDTO vehicleDTO = mapToDTO(vehicle);
        
        // Delete the vehicle
        vehicleRepository.deleteById(id);
        
        // Publish vehicle deleted event
        vehiclePublisher.publishVehicleDeleted(vehicleDTO);
    }
    
    // Helper methods for mapping between DTO and entity
    private VehicleDTO mapToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setBrand(vehicle.getBrand());
        dto.setModel(vehicle.getModel());
        dto.setManufacturingYear(vehicle.getManufacturingYear());
        dto.setFuelType(vehicle.getFuelType());
        dto.setGarageId(vehicle.getGarage().getId());
        return dto;
    }
    
    private Vehicle mapToEntity(VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
//        vehicle.setId(dto.getId());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setManufacturingYear(dto.getManufacturingYear());
        vehicle.setFuelType(dto.getFuelType());
        // Garage is set separately
        return vehicle;
    }
}