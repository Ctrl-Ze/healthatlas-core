package com.healthatlas.core.bloodtests.repository;

import com.healthatlas.core.bloodtests.dto.BloodTestFlatRowDto;
import com.healthatlas.core.bloodtests.model.BloodTest;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BloodTestRepository implements PanacheRepositoryBase<BloodTest, UUID> {

    public List<BloodTest> listByUser(UUID userRef) {
        return find("userRef", userRef).list();
    }

    public List<BloodTestFlatRowDto> findDetailedBloodTestsByUserId(UUID userId) {
        return getEntityManager()
                .createQuery("""
                    SELECT new com.healthatlas.core.bloodtests.dto.BloodTestFlatRowDto(
                        bt.id, bt.confirmed, bt.timestamp, bt.createdAt, bt.updatedAt,
                        a.name, btr.value, a.unit, a.normalLow, a.normalHigh
                    )
                    FROM BloodTest bt
                    LEFT JOIN BloodTestResult btr ON btr.bloodTest.id = bt.id
                    LEFT JOIN Analyte a ON a.id = btr.analyte.id
                    WHERE bt.userRef = :userId
                """, BloodTestFlatRowDto.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}