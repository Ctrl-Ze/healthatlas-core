package com.healthatlas.core.bloodtests.ports.outbound;

import com.healthatlas.core.bloodtests.domain.model.BloodTestResult;

import java.util.List;

public interface BloodTestResultRepositoryPort {
    List<BloodTestResult> save(List<BloodTestResult> results);
}
