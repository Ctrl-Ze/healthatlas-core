package com.healthatlas.core.bloodtests.events;

import java.time.Instant;
import java.util.UUID;

public record BloodTestRecordedEvent(
        UUID bloodTestId,
        UUID userId,
        Instant timestamp
) {
}
