package com.garagetest.messaging;

import com.garagetest.dto.VehicleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEvent {
    
    public enum EventType {
        CREATED,
        UPDATED,
        DELETED
    }
    
    private EventType eventType;
    private VehicleDTO vehicle;
    private LocalDateTime timestamp;
    
    public static VehicleEvent vehicleCreated(VehicleDTO vehicle) {
        return new VehicleEvent(EventType.CREATED, vehicle, LocalDateTime.now());
    }
    
    public static VehicleEvent vehicleUpdated(VehicleDTO vehicle) {
        return new VehicleEvent(EventType.UPDATED, vehicle, LocalDateTime.now());
    }
    
    public static VehicleEvent vehicleDeleted(VehicleDTO vehicle) {
        return new VehicleEvent(EventType.DELETED, vehicle, LocalDateTime.now());
    }
}