package com.healthatlas.core.bloodtests.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "blood_test_results")
public class BloodTestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @ManyToOne
    @JoinColumn(name = "blood_test_id", nullable = false)
    public BloodTest bloodTest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "analyte_id", referencedColumnName = "id", nullable = false)
    public Analyte analyte;

    @Column(nullable = false, precision = 5, scale = 2)
    public BigDecimal value;

    @Column(name = "created_at")
    public Instant createdAt = Instant.now();

    public BloodTestResult() {
    }

    public BloodTestResult(BloodTest bloodTest, Analyte analyte, BigDecimal value) {
        this.bloodTest = bloodTest;
        this.analyte = analyte;
        this.value = value;
    }

    public BloodTestResult(Analyte analyte, BigDecimal value) {
        this.analyte = analyte;
        this.value = value;
    }

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }
}