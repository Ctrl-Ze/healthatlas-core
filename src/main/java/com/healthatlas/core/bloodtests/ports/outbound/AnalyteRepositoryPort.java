package com.healthatlas.core.bloodtests.ports.outbound;

import com.healthatlas.core.bloodtests.domain.model.Analyte;

import java.util.List;

public interface AnalyteRepositoryPort {
    List<Analyte> getAll();
}
