package com.garagetest.service;

import com.garagetest.dto.VehicleDTO;
import com.garagetest.messaging.VehiclePublisher;
import com.garagetest.model.Garage;
import com.garagetest.model.Vehicle;
import com.garagetest.repository.GarageRepository;
import com.garagetest.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private VehiclePublisher vehiclePublisher;

    @InjectMocks
    private VehicleService vehicleService;

    private Garage garage;
    private Vehicle vehicle;
    private VehicleDTO vehicleDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        garage = new Garage();
        garage.setId(1L);
        garage.setName("Test Garage");
        garage.setVehicles(new ArrayList<>());

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setBrand("Renault");
        vehicle.setModel("Clio");
        vehicle.setManufacturingYear(2022);
        vehicle.setFuelType("Gasoline");
        vehicle.setGarage(garage);

        vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(1L);
        vehicleDTO.setBrand("Renault");
        vehicleDTO.setModel("Clio");
        vehicleDTO.setManufacturingYear(2022);
        vehicleDTO.setFuelType("Gasoline");
        vehicleDTO.setGarageId(1L);
    }

    @Test
    void createVehicle_ShouldReturnCreatedVehicle_WhenGarageHasCapacity() {
        // Arrange
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        doNothing().when(vehiclePublisher).publishVehicleCreated(any(VehicleDTO.class));

        // Act
        VehicleDTO result = vehicleService.createVehicle(vehicleDTO);

        // Assert
        assertNotNull(result);
        assertEquals(vehicleDTO.getId(), result.getId());
        assertEquals(vehicleDTO.getBrand(), result.getBrand());
        verify(garageRepository, times(1)).findById(1L);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
        verify(vehiclePublisher, times(1)).publishVehicleCreated(any(VehicleDTO.class));
    }

    @Test
    void createVehicle_ShouldThrowException_WhenGarageDoesNotExist() {
        // Arrange
        when(garageRepository.findById(999L)).thenReturn(Optional.empty());
        vehicleDTO.setGarageId(999L);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> vehicleService.createVehicle(vehicleDTO));
        verify(garageRepository, times(1)).findById(999L);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
        verify(vehiclePublisher, never()).publishVehicleCreated(any(VehicleDTO.class));
    }

    @Test
    void createVehicle_ShouldThrowException_WhenGarageIsAtCapacity() {
        // Arrange
        // Create a garage with 50 vehicles (at capacity)
        Garage fullGarage = new Garage();
        fullGarage.setId(2L);
        fullGarage.setName("Full Garage");
        List<Vehicle> vehicles = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Vehicle v = new Vehicle();
            v.setId((long) (i + 100));
            vehicles.add(v);
        }
        fullGarage.setVehicles(vehicles);

        when(garageRepository.findById(2L)).thenReturn(Optional.of(fullGarage));
        vehicleDTO.setGarageId(2L);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> vehicleService.createVehicle(vehicleDTO));
        verify(garageRepository, times(1)).findById(2L);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
        verify(vehiclePublisher, never()).publishVehicleCreated(any(VehicleDTO.class));
    }

    @Test
    void getVehicleById_ShouldReturnVehicle_WhenVehicleExists() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        // Act
        VehicleDTO result = vehicleService.getVehicleById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(vehicleDTO.getId(), result.getId());
        assertEquals(vehicleDTO.getBrand(), result.getBrand());
        verify(vehicleRepository, times(1)).findById(1L);
    }

    @Test
    void getVehicleById_ShouldThrowException_WhenVehicleDoesNotExist() {
        // Arrange
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> vehicleService.getVehicleById(999L));
        verify(vehicleRepository, times(1)).findById(999L);
    }

    @Test
    void updateVehicle_ShouldReturnUpdatedVehicle_WhenVehicleExists() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        doNothing().when(vehiclePublisher).publishVehicleUpdated(any(VehicleDTO.class));

        // Update DTO
        vehicleDTO.setModel("Megane");

        // Act
        VehicleDTO result = vehicleService.updateVehicle(1L, vehicleDTO);

        // Assert
        assertNotNull(result);
        assertEquals(vehicleDTO.getModel(), result.getModel());
        verify(vehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
        verify(vehiclePublisher, times(1)).publishVehicleUpdated(any(VehicleDTO.class));
    }

    @Test
    void deleteVehicle_ShouldDeleteVehicle_WhenVehicleExists() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        doNothing().when(vehicleRepository).deleteById(1L);
        doNothing().when(vehiclePublisher).publishVehicleDeleted(any(VehicleDTO.class));

        // Act
        vehicleService.deleteVehicle(1L);

        // Assert
        verify(vehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, times(1)).deleteById(1L);
        verify(vehiclePublisher, times(1)).publishVehicleDeleted(any(VehicleDTO.class));
    }
}