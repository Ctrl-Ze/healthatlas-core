package com.healthatlas.core.bloodtests.repository;

import com.healthatlas.core.bloodtests.model.BloodTestResult;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class BloodTestResultRepository implements PanacheRepositoryBase<BloodTestResult, UUID> {
}