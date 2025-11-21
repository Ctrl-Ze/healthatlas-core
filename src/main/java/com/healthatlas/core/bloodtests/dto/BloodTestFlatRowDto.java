package com.healthatlas.core.bloodtests.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BloodTestFlatRowDto(
        UUID id,
        boolean confirmed,
        Instant timestamp,
        Instant createdAt,
        Instant updatedAt,
        String analyteName,
        BigDecimal value,
        String unit,
        BigDecimal normalLow,
        BigDecimal normalHigh
) {}
