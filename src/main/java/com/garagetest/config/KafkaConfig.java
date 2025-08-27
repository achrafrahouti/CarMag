package com.garagetest.config;

import com.garagetest.messaging.VehicleEvent;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:garage-service}")
    private String groupId;

    public static final String VEHICLE_TOPIC = "vehicles";
//    @Bean
//    public KafkaAdmin kafkaAdmin() {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        configs.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 60000);
//        configs.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, 60000);
//        return new KafkaAdmin(configs);
//    }

    @Bean
    public NewTopic vehicleTopic() {
        return TopicBuilder.name(VEHICLE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    // Producer configuration
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // Consumer configuration
    @Bean
    public ConsumerFactory<String, VehicleEvent> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        configProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
        configProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "300000");
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        configProps.put(JsonDeserializer.TYPE_MAPPINGS, "vehicleEvent:com.garagetest.messaging.VehicleEvent");
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, "false");
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.garagetest.messaging.VehicleEvent");
        return new DefaultKafkaConsumerFactory<>(configProps,
                new StringDeserializer(),
                new JsonDeserializer<>(VehicleEvent.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, VehicleEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, VehicleEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}