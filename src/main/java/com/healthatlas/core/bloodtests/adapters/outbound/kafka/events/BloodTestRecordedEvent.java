package com.healthatlas.core.bloodtests.adapters.outbound.kafka.events;

import java.time.Instant;
import java.util.UUID;

public record BloodTestRecordedEvent(
        UUID bloodTestId,
        UUID userId,
        Instant timestamp
) {
}
