package com.garagetest.service;

import com.garagetest.dto.GarageDTO;
import com.garagetest.model.Garage;
import com.garagetest.repository.GarageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GarageService {
    
    private final GarageRepository garageRepository;
    
    public GarageDTO createGarage(GarageDTO garageDTO) {
        Garage garage = mapToEntity(garageDTO);
        Garage savedGarage = garageRepository.save(garage);
        return mapToDTO(savedGarage);
    }
    
    @Transactional(readOnly = true)
    public GarageDTO getGarageById(Long id) {
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Garage not found with id: " + id));
        return mapToDTO(garage);
    }
    
    @Transactional(readOnly = true)
    public Page<GarageDTO> getAllGarages(Pageable pageable) {
        return garageRepository.findAll(pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<GarageDTO> getGaragesByName(String name, Pageable pageable) {
        return garageRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<GarageDTO> getGaragesByCity(String city, Pageable pageable) {
        return garageRepository.findByCity(city, pageable)
                .map(this::mapToDTO);
    }
    
    public GarageDTO updateGarage(Long id, GarageDTO garageDTO) {
        Garage existingGarage = garageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Garage not found with id: " + id));
        
        existingGarage.setName(garageDTO.getName());
        existingGarage.setAddress(garageDTO.getAddress());
        existingGarage.setTelephone(garageDTO.getTelephone());
        existingGarage.setEmail(garageDTO.getEmail());
        existingGarage.setOpeningHours(garageDTO.getOpeningHours());
        
        Garage updatedGarage = garageRepository.save(existingGarage);
        return mapToDTO(updatedGarage);
    }
    
    public void deleteGarage(Long id) {
        if (!garageRepository.existsById(id)) {
            throw new EntityNotFoundException("Garage not found with id: " + id);
        }
        garageRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<GarageDTO> getGaragesByVehicleBrand(String brand) {
        return garageRepository.findByVehicleBrand(brand).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<GarageDTO> getGaragesByVehicleModel(String model) {
        return garageRepository.findByVehicleModel(model).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<GarageDTO> getGaragesByAccessoryType(String accessoryType) {
        return garageRepository.findByAccessoryType(accessoryType).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    // Helper methods for mapping between DTO and entity
    private GarageDTO mapToDTO(Garage garage) {
        GarageDTO dto = new GarageDTO();
        dto.setId(garage.getId());
        dto.setName(garage.getName());
        dto.setAddress(garage.getAddress());
        dto.setTelephone(garage.getTelephone());
        dto.setEmail(garage.getEmail());
        dto.setOpeningHours(garage.getOpeningHours());
        return dto;
    }
    
    private Garage mapToEntity(GarageDTO dto) {
        Garage garage = new Garage();
        garage.setId(dto.getId());
        garage.setName(dto.getName());
        garage.setAddress(dto.getAddress());
        garage.setTelephone(dto.getTelephone());
        garage.setEmail(dto.getEmail());
        garage.setOpeningHours(dto.getOpeningHours());
        return garage;
    }
}