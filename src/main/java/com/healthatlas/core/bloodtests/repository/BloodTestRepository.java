package com.healthatlas.core.bloodtests.repository;

import com.healthatlas.core.bloodtests.model.BloodTest;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BloodTestRepository implements PanacheRepository<BloodTest> {

    public List<BloodTest> listByUser(UUID userRef) {
        return find("userRef", userRef).list();
    }
}