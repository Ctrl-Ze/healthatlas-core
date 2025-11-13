package com.healthatlas.core.bloodtests.dto.request;

import com.healthatlas.core.bloodtests.model.BloodTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class BloodTestDto {

    public UUID userRef;
    public Instant timestamp;
    public boolean confirmed;

    public List<ResultDTO> results;

    public static class ResultDTO {
        public String analyte;   // e.g., "HGB"
        public BigDecimal value; // e.g., 13.5
    }
}
