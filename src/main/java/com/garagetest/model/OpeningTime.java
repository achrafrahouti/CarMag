package com.garagetest.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OpeningTime {
    @NotNull
    private LocalTime startTime;
    
    @NotNull
    private LocalTime endTime;
}