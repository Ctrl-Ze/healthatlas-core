package com.healthatlas.core.bloodtests.ports.outbound;

import com.healthatlas.core.bloodtests.adapters.inbound.rest.dto.response.BloodTestResponseDto;
import com.healthatlas.core.bloodtests.domain.model.BloodTest;

import java.util.List;
import java.util.UUID;

public interface BloodTestRepositoryPort {
    List<BloodTest> listByUser(UUID userRef);
    List<BloodTestResponseDto> findDetailedBloodTestsByUserId(UUID userId);
    BloodTest save(BloodTest bloodTest);
}
