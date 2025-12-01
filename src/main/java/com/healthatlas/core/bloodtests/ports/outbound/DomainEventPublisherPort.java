package com.healthatlas.core.bloodtests.ports.outbound;

import com.healthatlas.core.bloodtests.domain.model.BloodTest;

import java.util.UUID;

public interface DomainEventPublisherPort {
    void publish(BloodTest bloodTest, UUID userId);
}
