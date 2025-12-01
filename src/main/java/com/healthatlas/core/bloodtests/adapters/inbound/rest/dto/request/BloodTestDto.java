package com.healthatlas.core.bloodtests.adapters.inbound.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class BloodTestDto {

    @NotNull(message = "Timestamp is required")
    public Instant timestamp;

    public boolean confirmed;

    @NotNull(message = "Results list must be supplied.")
    @Size(min = 1, message = "At least one analyte result is required.")
    @Valid
    public List<ResultDto> results;

    public static class ResultDto {
        @NotBlank(message = "Analyte code must not be blank.")
        public String analyte;   // e.g., "HGB"

        @NotNull(message = "Analyte value is required.")
        public BigDecimal value; // e.g., 13.5
    }
}
