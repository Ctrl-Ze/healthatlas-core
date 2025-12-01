package com.healthatlas.core.bloodtests.adapters.inbound.rest.dto.response;

import com.healthatlas.core.bloodtests.adapters.outbound.db.dto.BloodTestFlatRowDto;
import com.healthatlas.core.bloodtests.domain.model.BloodTestResult;

import java.math.BigDecimal;

public record BloodTestResultDto(
    String analyte,
    BigDecimal value,
    String unit,
    BigDecimal normalLow,
    BigDecimal normalHigh
) {
    public static BloodTestResultDto from(BloodTestResult results) {
        return new BloodTestResultDto(
                results.analyte.name,
                results.value,
                results.analyte.unit,
                results.analyte.normalLow,
                results.analyte.normalHigh
        );
    }

    public static BloodTestResultDto from(BloodTestFlatRowDto rows) {
        return new BloodTestResultDto(
                rows.analyteName(),
                rows.value(),
                rows.unit(),
                rows.normalLow(),
                rows.normalHigh()
        );
    }
}
