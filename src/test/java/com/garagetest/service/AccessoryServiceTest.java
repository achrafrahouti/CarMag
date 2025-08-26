package com.garagetest.service;

import com.garagetest.dto.AccessoryDTO;
import com.garagetest.model.Accessory;
import com.garagetest.model.Vehicle;
import com.garagetest.repository.AccessoryRepository;
import com.garagetest.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessoryServiceTest {

    @Mock
    private AccessoryRepository accessoryRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private AccessoryService accessoryService;

    private Vehicle vehicle;
    private Accessory accessory;
    private AccessoryDTO accessoryDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setBrand("Renault");
        vehicle.setModel("Clio");

        accessory = new Accessory();
        accessory.setId(1L);
        accessory.setName("GPS Navigation");
        accessory.setDescription("Advanced GPS navigation system");
        accessory.setPrice(new BigDecimal("299.99"));
        accessory.setType("Electronics");
        accessory.setVehicle(vehicle);

        accessoryDTO = new AccessoryDTO();
        accessoryDTO.setId(1L);
        accessoryDTO.setName("GPS Navigation");
        accessoryDTO.setDescription("Advanced GPS navigation system");
        accessoryDTO.setPrice(new BigDecimal("299.99"));
        accessoryDTO.setType("Electronics");
        accessoryDTO.setVehicleId(1L);
    }

    @Test
    void createAccessory_ShouldReturnCreatedAccessory_WhenVehicleExists() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(accessoryRepository.save(any(Accessory.class))).thenReturn(accessory);

        // Act
        AccessoryDTO result = accessoryService.createAccessory(accessoryDTO);

        // Assert
        assertNotNull(result);
        assertEquals(accessoryDTO.getId(), result.getId());
        assertEquals(accessoryDTO.getName(), result.getName());
        verify(vehicleRepository, times(1)).findById(1L);
        verify(accessoryRepository, times(1)).save(any(Accessory.class));
    }

    @Test
    void createAccessory_ShouldThrowException_WhenVehicleDoesNotExist() {
        // Arrange
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());
        accessoryDTO.setVehicleId(999L);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> accessoryService.createAccessory(accessoryDTO));
        verify(vehicleRepository, times(1)).findById(999L);
        verify(accessoryRepository, never()).save(any(Accessory.class));
    }

    @Test
    void getAccessoryById_ShouldReturnAccessory_WhenAccessoryExists() {
        // Arrange
        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));

        // Act
        AccessoryDTO result = accessoryService.getAccessoryById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(accessoryDTO.getId(), result.getId());
        assertEquals(accessoryDTO.getName(), result.getName());
        verify(accessoryRepository, times(1)).findById(1L);
    }

    @Test
    void getAccessoryById_ShouldThrowException_WhenAccessoryDoesNotExist() {
        // Arrange
        when(accessoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> accessoryService.getAccessoryById(999L));
        verify(accessoryRepository, times(1)).findById(999L);
    }

    @Test
    void getAccessoriesByVehicleId_ShouldReturnAccessories_WhenVehicleExists() {
        // Arrange
        when(vehicleRepository.existsById(1L)).thenReturn(true);
        when(accessoryRepository.findByVehicleId(1L)).thenReturn(List.of(accessory));

        // Act
        List<AccessoryDTO> result = accessoryService.getAccessoriesByVehicleId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(accessoryDTO.getId(), result.get(0).getId());
        verify(vehicleRepository, times(1)).existsById(1L);
        verify(accessoryRepository, times(1)).findByVehicleId(1L);
    }

    @Test
    void getAccessoriesByVehicleId_ShouldThrowException_WhenVehicleDoesNotExist() {
        // Arrange
        when(vehicleRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> accessoryService.getAccessoriesByVehicleId(999L));
        verify(vehicleRepository, times(1)).existsById(999L);
        verify(accessoryRepository, never()).findByVehicleId(anyLong());
    }

    @Test
    void updateAccessory_ShouldReturnUpdatedAccessory_WhenAccessoryExists() {
        // Arrange
        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(accessoryRepository.save(any(Accessory.class))).thenReturn(accessory);

        // Update DTO
        accessoryDTO.setName("Premium GPS Navigation");
        accessoryDTO.setPrice(new BigDecimal("349.99"));

        // Act
        AccessoryDTO result = accessoryService.updateAccessory(1L, accessoryDTO);

        // Assert
        assertNotNull(result);
        assertEquals(accessoryDTO.getName(), result.getName());
        assertEquals(accessoryDTO.getPrice(), result.getPrice());
        verify(accessoryRepository, times(1)).findById(1L);
        verify(accessoryRepository, times(1)).save(any(Accessory.class));
    }

    @Test
    void deleteAccessory_ShouldDeleteAccessory_WhenAccessoryExists() {
        // Arrange
        when(accessoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(accessoryRepository).deleteById(1L);

        // Act
        accessoryService.deleteAccessory(1L);

        // Assert
        verify(accessoryRepository, times(1)).existsById(1L);
        verify(accessoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAccessory_ShouldThrowException_WhenAccessoryDoesNotExist() {
        // Arrange
        when(accessoryRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> accessoryService.deleteAccessory(999L));
        verify(accessoryRepository, times(1)).existsById(999L);
        verify(accessoryRepository, never()).deleteById(999L);
    }
}