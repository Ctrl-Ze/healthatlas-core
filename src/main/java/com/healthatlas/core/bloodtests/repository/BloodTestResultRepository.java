package com.healthatlas.core.bloodtests.repository;

import com.healthatlas.core.bloodtests.model.BloodTestResult;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BloodTestResultRepository implements PanacheRepository<BloodTestResult> {
}