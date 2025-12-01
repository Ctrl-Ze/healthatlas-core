package com.healthatlas.core.bloodtests.adapters.outbound.db;

import com.healthatlas.core.bloodtests.adapters.inbound.rest.dto.request.BloodTestDto;
import com.healthatlas.core.bloodtests.adapters.inbound.rest.dto.response.BloodTestResponseDto;
import com.healthatlas.core.bloodtests.domain.model.BloodTest;
import com.healthatlas.core.bloodtests.adapters.outbound.db.dto.BloodTestFlatRowDto;
import com.healthatlas.core.bloodtests.ports.outbound.BloodTestRepositoryPort;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BloodTestRepositoryAdapter implements PanacheRepositoryBase<BloodTest, UUID>, BloodTestRepositoryPort {

    public List<BloodTest> listByUser(UUID userRef) {
        return find("userRef", userRef).list();
    }

    public List<BloodTestResponseDto> findDetailedBloodTestsByUserId(UUID userId) {
        var rows = getEntityManager()
                .createQuery("""
                    SELECT new com.healthatlas.core.bloodtests.adapters.outbound.db.dto.BloodTestFlatRowDto(
                        bt.id, bt.confirmed, bt.timestamp, bt.createdAt, bt.updatedAt,
                        a.name, btr.value, a.unit, a.normalLow, a.normalHigh
                    )
                    FROM BloodTest bt
                    LEFT JOIN BloodTestResult btr ON btr.bloodTest.id = bt.id
                    LEFT JOIN Analyte a ON a.id = btr.analyte.id
                    WHERE bt.userRef = :userId
                    ORDER BY bt.timestamp DESC
                """, BloodTestFlatRowDto.class)
                .setParameter("userId", userId)
                .getResultList();
        return BloodTestResponseDto.from(rows);
    }

    @Override
    public BloodTest save(BloodTest bloodTest) {
        persist(bloodTest);
        return bloodTest;
    }
}