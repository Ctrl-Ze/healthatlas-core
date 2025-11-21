package com.healthatlas.core.bloodtests.repository;

import com.healthatlas.core.bloodtests.model.Analyte;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class AnalyteRepository implements PanacheRepositoryBase<Analyte, UUID> {
}
