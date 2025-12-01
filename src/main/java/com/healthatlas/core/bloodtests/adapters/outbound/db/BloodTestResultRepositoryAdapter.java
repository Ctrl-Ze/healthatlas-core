package com.healthatlas.core.bloodtests.adapters.outbound.db;

import com.healthatlas.core.bloodtests.domain.model.BloodTestResult;
import com.healthatlas.core.bloodtests.ports.outbound.BloodTestResultRepositoryPort;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BloodTestResultRepositoryAdapter implements PanacheRepositoryBase<BloodTestResult, UUID>, BloodTestResultRepositoryPort {
    @Override
    public List<BloodTestResult> save(List<BloodTestResult> results) {
        persist(results);
        return results;
    }
}