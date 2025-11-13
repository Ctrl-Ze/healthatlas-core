package com.healthatlas.core.dto.response;

import com.healthatlas.core.bloodtests.model.BloodTestResult;

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
}
