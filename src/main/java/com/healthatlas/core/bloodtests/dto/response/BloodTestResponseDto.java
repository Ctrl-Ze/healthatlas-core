package com.healthatlas.core.bloodtests.dto.response;

import com.healthatlas.core.bloodtests.dto.BloodTestFlatRowDto;
import com.healthatlas.core.bloodtests.model.BloodTest;
import com.healthatlas.core.bloodtests.model.BloodTestResult;
import jakarta.ws.rs.NotFoundException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
                results.stream()
                        .map(BloodTestResultDto::from)
                        .toList()
        );
    }

    public static List<BloodTestResponseDto> from(List<BloodTestFlatRowDto> rows) {
        if (rows == null || rows.isEmpty()) {
            throw new NotFoundException("Blood tests not found");
        }

        return rows.stream()
                .collect(Collectors.groupingBy(BloodTestFlatRowDto::id))
                .values().stream()
                .map(group -> {
                    var first = group.getFirst();
                    var results = group.stream()
                            .filter(r -> r.analyteName() != null)
                            .map(BloodTestResultDto::from)
                            .toList();
                    return new BloodTestResponseDto(
                            first.id(),
                            first.confirmed(),
                            first.timestamp(),
                            first.createdAt(),
                            first.updatedAt(),
                            results
                    );
                })
                .toList();
    }
}

