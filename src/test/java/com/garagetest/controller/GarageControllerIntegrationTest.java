package com.garagetest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garagetest.dto.GarageDTO;
import com.garagetest.model.Garage;
import com.garagetest.repository.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class GarageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private GarageDTO garageDTO;

    @BeforeEach
    void setUp() {
        // Clear the repository
        garageRepository.deleteAll();

        // Setup test data
        Map<DayOfWeek, String> openingHours = new HashMap<>();
        openingHours.put(DayOfWeek.MONDAY, "09:00-18:00");
        openingHours.put(DayOfWeek.TUESDAY, "09:00-18:00");
        openingHours.put(DayOfWeek.WEDNESDAY, "09:00-18:00");
        openingHours.put(DayOfWeek.THURSDAY, "09:00-18:00");
        openingHours.put(DayOfWeek.FRIDAY, "09:00-18:00");

        garageDTO = new GarageDTO();
        garageDTO.setName("Test Garage");
        garageDTO.setAddress("123 Test Street, Test City");
        garageDTO.setTelephone("123-456-7890");
        garageDTO.setEmail("test@garage.com");
        garageDTO.setOpeningHours(openingHours);
    }

    @Test
    void createGarage_ShouldReturnCreatedGarage() throws Exception {
        // Act
        ResultActions response = mockMvc.perform(post("/api/garages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(garageDTO)));

        // Assert
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(garageDTO.getName())))
                .andExpect(jsonPath("$.address", is(garageDTO.getAddress())))
                .andExpect(jsonPath("$.telephone", is(garageDTO.getTelephone())))
                .andExpect(jsonPath("$.email", is(garageDTO.getEmail())));
    }

    @Test
    void getGarageById_ShouldReturnGarage_WhenGarageExists() throws Exception {
        // Arrange
        Garage savedGarage = new Garage();
        savedGarage.setName(garageDTO.getName());
        savedGarage.setAddress(garageDTO.getAddress());
        savedGarage.setTelephone(garageDTO.getTelephone());
        savedGarage.setEmail(garageDTO.getEmail());
        savedGarage.setOpeningHours(garageDTO.getOpeningHours());
        
        Garage persistedGarage = garageRepository.save(savedGarage);

        // Act
        ResultActions response = mockMvc.perform(get("/api/garages/{id}", persistedGarage.getId()));

        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(persistedGarage.getId().intValue())))
                .andExpect(jsonPath("$.name", is(persistedGarage.getName())))
                .andExpect(jsonPath("$.address", is(persistedGarage.getAddress())))
                .andExpect(jsonPath("$.telephone", is(persistedGarage.getTelephone())))
                .andExpect(jsonPath("$.email", is(persistedGarage.getEmail())));
    }

    @Test
    void getGarageById_ShouldReturn404_WhenGarageDoesNotExist() throws Exception {
        // Act
        ResultActions response = mockMvc.perform(get("/api/garages/{id}", 999L));

        // Assert
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllGarages_ShouldReturnGarages() throws Exception {
        // Arrange
        Garage garage1 = new Garage();
        garage1.setName("Garage 1");
        garage1.setAddress("Address 1");
        garage1.setTelephone("123-456-7890");
        garage1.setEmail("garage1@test.com");
        garage1.setOpeningHours(garageDTO.getOpeningHours());
        
        Garage garage2 = new Garage();
        garage2.setName("Garage 2");
        garage2.setAddress("Address 2");
        garage2.setTelephone("123-456-7891");
        garage2.setEmail("garage2@test.com");
        garage2.setOpeningHours(garageDTO.getOpeningHours());
        
        garageRepository.save(garage1);
        garageRepository.save(garage2);

        // Act
        ResultActions response = mockMvc.perform(get("/api/garages"));

        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(2)))
                .andExpect(jsonPath("$.content[0].name", is("Garage 1")))
                .andExpect(jsonPath("$.content[1].name", is("Garage 2")));
    }

    @Test
    void updateGarage_ShouldReturnUpdatedGarage_WhenGarageExists() throws Exception {
        // Arrange
        Garage savedGarage = new Garage();
        savedGarage.setName(garageDTO.getName());
        savedGarage.setAddress(garageDTO.getAddress());
        savedGarage.setTelephone(garageDTO.getTelephone());
        savedGarage.setEmail(garageDTO.getEmail());
        savedGarage.setOpeningHours(garageDTO.getOpeningHours());
        
        Garage persistedGarage = garageRepository.save(savedGarage);

        // Update DTO
        garageDTO.setName("Updated Garage");
        garageDTO.setAddress("Updated Address");

        // Act
        ResultActions response = mockMvc.perform(put("/api/garages/{id}", persistedGarage.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(garageDTO)));

        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(persistedGarage.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Updated Garage")))
                .andExpect(jsonPath("$.address", is("Updated Address")));
    }

    @Test
    void deleteGarage_ShouldReturn204_WhenGarageExists() throws Exception {
        // Arrange
        Garage savedGarage = new Garage();
        savedGarage.setName(garageDTO.getName());
        savedGarage.setAddress(garageDTO.getAddress());
        savedGarage.setTelephone(garageDTO.getTelephone());
        savedGarage.setEmail(garageDTO.getEmail());
        savedGarage.setOpeningHours(garageDTO.getOpeningHours());
        
        Garage persistedGarage = garageRepository.save(savedGarage);

        // Act
        ResultActions response = mockMvc.perform(delete("/api/garages/{id}", persistedGarage.getId()));

        // Assert
        response.andDo(print())
                .andExpect(status().isNoContent());

        // Verify the garage is deleted
        mockMvc.perform(get("/api/garages/{id}", persistedGarage.getId()))
                .andExpect(status().isNotFound());
    }
}