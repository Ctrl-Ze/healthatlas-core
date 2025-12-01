package com.healthatlas.core.bloodtests.ports.inbound;

import com.healthatlas.core.bloodtests.adapters.inbound.rest.dto.request.BloodTestDto;
import com.healthatlas.core.bloodtests.adapters.inbound.rest.dto.response.BloodTestResponseDto;

import java.util.List;
import java.util.UUID;

public interface BloodTestUseCase {
    List<BloodTestResponseDto> getBloodTestsPerUser(UUID userId);
    BloodTestResponseDto createBloodTest(UUID userId, BloodTestDto dto);
}
