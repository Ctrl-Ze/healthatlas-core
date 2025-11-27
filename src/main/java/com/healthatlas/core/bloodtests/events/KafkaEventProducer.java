package com.healthatlas.core.bloodtests.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class KafkaEventProducer {

    @Inject
    ObjectMapper mapper;

    @Channel("blood-test-events")
    Emitter<String> emitter;

    public void send(BloodTestRecordedEvent event) {
        try {
            System.out.println("Sending kafka message!");
            String json = mapper.writeValueAsString(event);
            emitter.send(json);
        } catch (JsonProcessingException e) {
            //TODO better Exception handling
            throw new RuntimeException("Failed to serialize BloodTestRecordedEvent", e);
        }
    }
}
