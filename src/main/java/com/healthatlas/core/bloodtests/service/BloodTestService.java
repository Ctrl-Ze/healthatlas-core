package com.healthatlas.core.bloodtests.service;

import com.healthatlas.core.bloodtests.cache.AnalyteCache;
import com.healthatlas.core.bloodtests.dto.request.BloodTestDto;
import com.healthatlas.core.bloodtests.dto.response.BloodTestResponseDto;
import com.healthatlas.core.bloodtests.events.BloodTestRecordedEvent;
import com.healthatlas.core.bloodtests.events.KafkaEventProducer;
import com.healthatlas.core.bloodtests.model.Analyte;
import com.healthatlas.core.bloodtests.model.BloodTest;
import com.healthatlas.core.bloodtests.model.BloodTestResult;
import com.healthatlas.core.bloodtests.repository.BloodTestRepository;
import com.healthatlas.core.bloodtests.repository.BloodTestResultRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BloodTestService {

    private static final Logger log = LoggerFactory.getLogger(BloodTestService.class);
    @Inject
    BloodTestRepository bloodTestRepository;

    @Inject
    BloodTestResultRepository bloodTestResultRepository;

    @Inject
    AnalyteCache analyteCache;

    @Inject
    KafkaEventProducer kafkaEventProducer;

    // TODO add pagination
    public List<BloodTestResponseDto> getBloodTestsPerUser(SecurityContext ctx) {
        UUID userId = getCurrentUserUuid(ctx);

        var res = bloodTestRepository.findDetailedBloodTestsByUserId(userId);

        return BloodTestResponseDto.from(res);
    }

    @Transactional
    public BloodTestResponseDto createBloodTest(SecurityContext ctx, BloodTestDto dto) {
        UUID userId = getCurrentUserUuid(ctx);

        var results = dto.results.stream()
                .map(resDTO -> {
                    Analyte analyte = analyteCache.get(resDTO.analyte);
                    return new BloodTestResult(analyte, resDTO.value);
                }).toList();


        BloodTest bloodTest = BloodTest.of(dto, userId);
        bloodTestRepository.persist(bloodTest);

        results.forEach(r -> r.bloodTest = bloodTest);
        bloodTestResultRepository.persist(results);

        kafkaEventProducer.send(new BloodTestRecordedEvent(
                bloodTest.id,
                userId,
                bloodTest.timestamp
        ));
        log.info("Event was sent to Kafka");

        return BloodTestResponseDto.from(bloodTest, results);
    }

    private UUID getCurrentUserUuid(SecurityContext ctx) {
        JsonWebToken jwt = (JsonWebToken) ctx.getUserPrincipal();
        return UUID.fromString(jwt.getClaim("user_id"));
    }
}