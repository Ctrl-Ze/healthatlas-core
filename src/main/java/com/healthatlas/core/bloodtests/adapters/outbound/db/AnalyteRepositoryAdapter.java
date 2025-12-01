package com.healthatlas.core.bloodtests.adapters.outbound.db;

import com.healthatlas.core.bloodtests.domain.model.Analyte;
import com.healthatlas.core.bloodtests.ports.outbound.AnalyteRepositoryPort;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AnalyteRepositoryAdapter implements PanacheRepositoryBase<Analyte, UUID>, AnalyteRepositoryPort {
    @Override
    public List<Analyte> getAll() {
        return findAll().list();
    }
}
