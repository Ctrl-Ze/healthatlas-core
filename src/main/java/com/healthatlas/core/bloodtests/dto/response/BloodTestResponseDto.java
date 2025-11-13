package com.healthatlas.core.bloodtests.dto.response;

import com.healthatlas.core.bloodtests.model.BloodTest;
import com.healthatlas.core.bloodtests.model.BloodTestResult;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record BloodTestResponseDto(
        UUID id,
        boolean confirmed,
        Instant timestamp,
        Instant createdAt,
        Instant updatedAt,
        List<BloodTestResultDto> results
) {
    public static BloodTestResponseDto from(BloodTest bloodTest, List<BloodTestResult> results) {
        return new BloodTestResponseDto(
                bloodTest.id,
                bloodTest.confirmed,
                bloodTest.timestamp,
                bloodTest.createdAt,
                bloodTest.updatedAt,
                results.stream().map(BloodTestResultDto::from).toList()
        );
    }
}

