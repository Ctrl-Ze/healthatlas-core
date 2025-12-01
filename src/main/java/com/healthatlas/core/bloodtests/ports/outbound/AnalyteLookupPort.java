package com.healthatlas.core.bloodtests.ports.outbound;

import com.healthatlas.core.bloodtests.domain.model.Analyte;

public interface AnalyteLookupPort {
    Analyte getByName(String name);
    void reload();
}
