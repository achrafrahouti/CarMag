package com.garagetest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private Long id;
    
    @NotBlank(message = "Brand is required")
    private String brand;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Manufacturing year is required")
    @Past(message = "Manufacturing year must be in the past")
    private Integer manufacturingYear;
    
    @NotBlank(message = "Fuel type is required")
    private String fuelType;
    
    private Long garageId;
}