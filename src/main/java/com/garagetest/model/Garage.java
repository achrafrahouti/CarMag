package com.garagetest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Garage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String telephone;

    @NotBlank
    @Email
    private String email;

    @ElementCollection
    @CollectionTable(name = "garage_opening_hours", 
                    joinColumns = @JoinColumn(name = "garage_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "opening_times")
    private Map<DayOfWeek, String> openingHours = new HashMap<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();

    // Business constraint: Maximum 50 vehicles per garage
    public boolean canAddVehicle() {
        return vehicles.size() < 50;
    }
}