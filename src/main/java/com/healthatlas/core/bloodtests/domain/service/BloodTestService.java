package com.healthatlas.core.bloodtests.domain.service;

import com.healthatlas.core.bloodtests.adapters.inbound.rest.dto.request.BloodTestDto;
import com.healthatlas.core.bloodtests.adapters.inbound.rest.dto.response.BloodTestResponseDto;
import com.healthatlas.core.bloodtests.domain.model.Analyte;
import com.healthatlas.core.bloodtests.domain.model.BloodTest;
import com.healthatlas.core.bloodtests.domain.model.BloodTestResult;
import com.healthatlas.core.bloodtests.ports.inbound.BloodTestUseCase;
import com.healthatlas.core.bloodtests.ports.outbound.AnalyteLookupPort;
import com.healthatlas.core.bloodtests.ports.outbound.BloodTestRepositoryPort;
import com.healthatlas.core.bloodtests.ports.outbound.BloodTestResultRepositoryPort;
import com.healthatlas.core.bloodtests.ports.outbound.DomainEventPublisherPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BloodTestService implements BloodTestUseCase {

    private static final Logger log = LoggerFactory.getLogger(BloodTestService.class);
    @Inject
    BloodTestRepositoryPort bloodTestRepoPort;

    @Inject
    BloodTestResultRepositoryPort bloodTestResultRepoPort;

    @Inject
    AnalyteLookupPort analyteCache;

    @Inject
    DomainEventPublisherPort domainEventPublisherPort;

    // TODO add pagination
    public List<BloodTestResponseDto> getBloodTestsPerUser(UUID userId) {
        return bloodTestRepoPort.findDetailedBloodTestsByUserId(userId);
    }

    @Transactional
    public BloodTestResponseDto createBloodTest(UUID userId, BloodTestDto dto) {

        var results = dto.results.stream()
                .map(resDTO -> {
                    Analyte analyte = analyteCache.getByName(resDTO.analyte);
                    return new BloodTestResult(analyte, resDTO.value);
                }).toList();


        BloodTest bloodTest = BloodTest.from(dto, userId);
        bloodTestRepoPort.save(bloodTest);

        results.forEach(r -> r.bloodTest = bloodTest);
        bloodTestResultRepoPort.save(results);

        domainEventPublisherPort.publish(bloodTest, userId);
        log.info("Event was sent to Kafka");

        return BloodTestResponseDto.from(bloodTest, results);
    }
}