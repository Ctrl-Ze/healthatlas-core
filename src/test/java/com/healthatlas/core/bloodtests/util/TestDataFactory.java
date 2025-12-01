package com.healthatlas.core.bloodtests.util;

import com.healthatlas.core.bloodtests.domain.model.Analyte;
import com.healthatlas.core.bloodtests.domain.model.BloodTest;
import com.healthatlas.core.bloodtests.domain.model.BloodTestResult;
import com.healthatlas.core.bloodtests.adapters.outbound.db.AnalyteRepositoryAdapter;
import com.healthatlas.core.bloodtests.adapters.outbound.db.BloodTestRepositoryAdapter;
import com.healthatlas.core.bloodtests.adapters.outbound.db.BloodTestResultRepositoryAdapter;
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
    AnalyteRepositoryAdapter analyteRepository;
    @Inject
    BloodTestRepositoryAdapter bloodTestRepository;
    @Inject
    BloodTestResultRepositoryAdapter resultRepository;

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
