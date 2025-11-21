package com.healthatlas.core.bloodtests.util;

import com.healthatlas.core.bloodtests.model.Analyte;
import com.healthatlas.core.bloodtests.model.BloodTest;
import com.healthatlas.core.bloodtests.model.BloodTestResult;
import com.healthatlas.core.bloodtests.repository.AnalyteRepository;
import com.healthatlas.core.bloodtests.repository.BloodTestRepository;
import com.healthatlas.core.bloodtests.repository.BloodTestResultRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class TestDataFactory {

    @Inject
    AnalyteRepository analyteRepository;
    @Inject
    BloodTestRepository bloodTestRepository;
    @Inject
    BloodTestResultRepository resultRepository;

    @Transactional
    public BloodTest createBloodTest(UUID userId, Instant timestamp, Map<String, BigDecimal> values) {
        BloodTest test = new BloodTest();
        test.userRef = userId;
        test.timestamp = timestamp;
        test.confirmed = true;

        bloodTestRepository.persist(test);

        values.forEach((analyteName, val) -> {
            Analyte analyte = analyteRepository.find("name", analyteName).firstResult();
            BloodTestResult result = new BloodTestResult(test, analyte, val);
            resultRepository.persist(result);
        });

        return test;
    }

}
