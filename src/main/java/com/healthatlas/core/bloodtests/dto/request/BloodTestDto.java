package com.healthatlas.core.bloodtests.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class BloodTestDto {

    // TODO: add validation annotations later
    public Instant timestamp;
    public boolean confirmed;

    public List<ResultDTO> results;

    public static class ResultDTO {
        public String analyte;   // e.g., "HGB"
        public BigDecimal value; // e.g., 13.5
    }
}
