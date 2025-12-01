package com.healthatlas.core.bloodtests.adapters.outbound.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthatlas.core.bloodtests.adapters.outbound.kafka.events.BloodTestRecordedEvent;
import com.healthatlas.core.bloodtests.domain.model.BloodTest;
import com.healthatlas.core.bloodtests.ports.outbound.DomainEventPublisherPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.UUID;

@ApplicationScoped
public class KafkaEventProducer implements DomainEventPublisherPort {

    @Inject
    ObjectMapper mapper;

    @Channel("blood-test-events")
    Emitter<String> emitter;

    @Override
    public void publish(BloodTest bloodTest, UUID userId) {
        var event = new BloodTestRecordedEvent(
                bloodTest.id,
                userId,
                bloodTest.timestamp
        );
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
