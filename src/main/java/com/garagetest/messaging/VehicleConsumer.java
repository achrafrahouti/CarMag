package com.garagetest.messaging;

import com.garagetest.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VehicleConsumer {
    
    @KafkaListener(topics = KafkaConfig.VEHICLE_TOPIC, groupId = "${spring.kafka.consumer.group-id:garage-service}")
    public void consumeVehicleEvent(@Payload VehicleEvent event) {
        log.info("Received vehicle event: {} for vehicle ID: {}", 
                event.getEventType(), 
                event.getVehicle().getId());
        
        switch (event.getEventType()) {
            case CREATED:
                handleVehicleCreated(event);
                break;
            case UPDATED:
                handleVehicleUpdated(event);
                break;
            case DELETED:
                handleVehicleDeleted(event);
                break;
            default:
                log.warn("Unknown event type: {}", event.getEventType());
        }
    }
    
    private void handleVehicleCreated(VehicleEvent event) {
        log.info("Processing vehicle created event for vehicle: {} - {}", 
                event.getVehicle().getId(),
                event.getVehicle().getBrand() + " " + event.getVehicle().getModel());

        //print a big message to indicate a vehicle was created
        log.info("**************************************************");
        log.info("**************************************************");
        log.info("**************************************************");
        log.info("**************************************************");
        log.info("********** VEHICLE CREATED EVENT **********");
        log.info("********** VEHICLE CREATED EVENT **********");
        log.info("********** VEHICLE CREATED EVENT **********");
        log.info("********** VEHICLE CREATED EVENT **********");
        log.info("**************************************************");
        log.info("**************************************************");
        log.info("**************************************************");
        log.info("**************************************************");
        // Here you would implement any business logic needed when a vehicle is created
        // For example, sending notifications, updating statistics, etc.
    }
    
    private void handleVehicleUpdated(VehicleEvent event) {
        log.info("Processing vehicle updated event for vehicle: {} - {}", 
                event.getVehicle().getId(),
                event.getVehicle().getBrand() + " " + event.getVehicle().getModel());
        
        // Here you would implement any business logic needed when a vehicle is updated
    }
    
    private void handleVehicleDeleted(VehicleEvent event) {
        log.info("Processing vehicle deleted event for vehicle: {} - {}", 
                event.getVehicle().getId(),
                event.getVehicle().getBrand() + " " + event.getVehicle().getModel());
        
        // Here you would implement any business logic needed when a vehicle is deleted
    }
}