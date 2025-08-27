package com.garagetest.service;

import com.garagetest.dto.GarageDTO;
import com.garagetest.model.Garage;
import com.garagetest.model.OpeningTime;
import com.garagetest.repository.GarageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GarageServiceTest {

    @Mock
    private GarageRepository garageRepository;

    @InjectMocks
    private GarageService garageService;

    private Garage garage;
    private GarageDTO garageDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        Map<DayOfWeek, List<OpeningTime>> openingHours = new HashMap<>();
        
        // Create opening times for each day
        List<OpeningTime> mondayHours = new ArrayList<>();
        mondayHours.add(new OpeningTime(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        
        List<OpeningTime> tuesdayHours = new ArrayList<>();
        tuesdayHours.add(new OpeningTime(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        
        List<OpeningTime> wednesdayHours = new ArrayList<>();
        wednesdayHours.add(new OpeningTime(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        
        List<OpeningTime> thursdayHours = new ArrayList<>();
        thursdayHours.add(new OpeningTime(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        
        List<OpeningTime> fridayHours = new ArrayList<>();
        fridayHours.add(new OpeningTime(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        
        openingHours.put(DayOfWeek.MONDAY, mondayHours);
        openingHours.put(DayOfWeek.TUESDAY, tuesdayHours);
        openingHours.put(DayOfWeek.WEDNESDAY, wednesdayHours);
        openingHours.put(DayOfWeek.THURSDAY, thursdayHours);
        openingHours.put(DayOfWeek.FRIDAY, fridayHours);

        garage = new Garage();
        garage.setId(1L);
        garage.setName("Test Garage");
        garage.setAddress("123 Test Street, Test City");
        garage.setTelephone("123-456-7890");
        garage.setEmail("test@garage.com");
        garage.setOpeningHours(openingHours);

        garageDTO = new GarageDTO();
        garageDTO.setId(1L);
        garageDTO.setName("Test Garage");
        garageDTO.setAddress("123 Test Street, Test City");
        garageDTO.setTelephone("123-456-7890");
        garageDTO.setEmail("test@garage.com");
        garageDTO.setOpeningHours(openingHours);
    }

    @Test
    void createGarage_ShouldReturnCreatedGarage() {
        // Arrange
        when(garageRepository.save(any(Garage.class))).thenReturn(garage);

        // Act
        GarageDTO result = garageService.createGarage(garageDTO);

        // Assert
        assertNotNull(result);
        assertEquals(garageDTO.getId(), result.getId());
        assertEquals(garageDTO.getName(), result.getName());
        verify(garageRepository, times(1)).save(any(Garage.class));
    }

    @Test
    void getGarageById_ShouldReturnGarage_WhenGarageExists() {
        // Arrange
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));

        // Act
        GarageDTO result = garageService.getGarageById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(garageDTO.getId(), result.getId());
        assertEquals(garageDTO.getName(), result.getName());
        verify(garageRepository, times(1)).findById(1L);
    }

    @Test
    void getGarageById_ShouldThrowException_WhenGarageDoesNotExist() {
        // Arrange
        when(garageRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> garageService.getGarageById(999L));
        verify(garageRepository, times(1)).findById(999L);
    }

    @Test
    void getAllGarages_ShouldReturnPageOfGarages() {
        // Arrange
        Page<Garage> garagePage = new PageImpl<>(List.of(garage));
        Pageable pageable = PageRequest.of(0, 10);
        when(garageRepository.findAll(pageable)).thenReturn(garagePage);

        // Act
        Page<GarageDTO> result = garageService.getAllGarages(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(garageDTO.getId(), result.getContent().get(0).getId());
        verify(garageRepository, times(1)).findAll(pageable);
    }

    @Test
    void updateGarage_ShouldReturnUpdatedGarage_WhenGarageExists() {
        // Arrange
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(garageRepository.save(any(Garage.class))).thenReturn(garage);

        // Update DTO
        garageDTO.setName("Updated Garage");

        // Act
        GarageDTO result = garageService.updateGarage(1L, garageDTO);

        // Assert
        assertNotNull(result);
        assertEquals(garageDTO.getName(), result.getName());
        verify(garageRepository, times(1)).findById(1L);
        verify(garageRepository, times(1)).save(any(Garage.class));
    }

    @Test
    void updateGarage_ShouldThrowException_WhenGarageDoesNotExist() {
        // Arrange
        when(garageRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> garageService.updateGarage(999L, garageDTO));
        verify(garageRepository, times(1)).findById(999L);
        verify(garageRepository, never()).save(any(Garage.class));
    }

    @Test
    void deleteGarage_ShouldDeleteGarage_WhenGarageExists() {
        // Arrange
        when(garageRepository.existsById(1L)).thenReturn(true);
        doNothing().when(garageRepository).deleteById(1L);

        // Act
        garageService.deleteGarage(1L);

        // Assert
        verify(garageRepository, times(1)).existsById(1L);
        verify(garageRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteGarage_ShouldThrowException_WhenGarageDoesNotExist() {
        // Arrange
        when(garageRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> garageService.deleteGarage(999L));
        verify(garageRepository, times(1)).existsById(999L);
        verify(garageRepository, never()).deleteById(999L);
    }
}