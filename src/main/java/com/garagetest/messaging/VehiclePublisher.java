package com.garagetest.messaging;

import com.garagetest.config.KafkaConfig;
import com.garagetest.dto.VehicleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiclePublisher {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public void publishVehicleCreated(VehicleDTO vehicleDTO) {
        VehicleEvent event = VehicleEvent.vehicleCreated(vehicleDTO);
        sendVehicleEvent(event);
        log.info("Published vehicle created event: {}", vehicleDTO.getId());
    }
    
    public void publishVehicleUpdated(VehicleDTO vehicleDTO) {
        VehicleEvent event = VehicleEvent.vehicleUpdated(vehicleDTO);
        sendVehicleEvent(event);
        log.info("Published vehicle updated event: {}", vehicleDTO.getId());
    }
    
    public void publishVehicleDeleted(VehicleDTO vehicleDTO) {
        VehicleEvent event = VehicleEvent.vehicleDeleted(vehicleDTO);
        sendVehicleEvent(event);
        log.info("Published vehicle deleted event: {}", vehicleDTO.getId());
    }
    
    private void sendVehicleEvent(VehicleEvent event) {
        try {
            kafkaTemplate.send(KafkaConfig.VEHICLE_TOPIC, event.getVehicle().getId().toString(), event);
        } catch (Exception e) {
            log.error("Error publishing vehicle event: {}", e.getMessage(), e);
        }
    }
}