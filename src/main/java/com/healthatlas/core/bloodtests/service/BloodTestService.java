package com.healthatlas.core.bloodtests.service;

import com.healthatlas.core.bloodtests.cache.AnalyteCache;
import com.healthatlas.core.bloodtests.repository.BloodTestRepository;
import com.healthatlas.core.bloodtests.repository.BloodTestResultRepository;
import com.healthatlas.core.dto.request.BloodTestDto;
import com.healthatlas.core.dto.response.BloodTestResponseDto;
import com.healthatlas.core.bloodtests.model.Analyte;
import com.healthatlas.core.bloodtests.model.BloodTest;
import com.healthatlas.core.bloodtests.model.BloodTestResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BloodTestService {

    @Inject
    BloodTestRepository bloodTestRepository;

    @Inject
    BloodTestResultRepository bloodTestResultRepository;

    @Inject
    AnalyteCache analyteCache;

    public List<BloodTest> getBloodTestsPerUser(SecurityContext ctx) {
        UUID userId = getCurrentUserUuid(ctx);
        return bloodTestRepository.listByUser(userId);
    }

    @Transactional
    public BloodTestResponseDto createBloodTest(SecurityContext ctx, BloodTestDto dto) {
        UUID userId = getCurrentUserUuid(ctx);

        if (dto.results == null || dto.results.isEmpty()) {
            throw new IllegalArgumentException("At least one analyte result is required.");
        }

        var results = dto.results.stream()
                .map(resDTO -> {
                    Analyte analyte = analyteCache.get(resDTO.analyte);
                    return new BloodTestResult(analyte, resDTO.value);
                }).toList();


        BloodTest bloodTest = BloodTest.of(dto, userId);
        bloodTestRepository.persist(bloodTest);

        results.forEach(r -> r.bloodTest = bloodTest);
        bloodTestResultRepository.persist(results);

        return BloodTestResponseDto.from(bloodTest, results);
    }

    private UUID getCurrentUserUuid(SecurityContext ctx) {
        JsonWebToken jwt = (JsonWebToken) ctx.getUserPrincipal();
        return UUID.fromString(jwt.getClaim("user_id"));
    }
}