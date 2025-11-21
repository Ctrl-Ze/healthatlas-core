package com.healthatlas.core.bloodtests.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "analytes")
public class Analyte extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false, unique = true)
    public String name;

    @Column(nullable = false)
    public String unit;

    @Column(name = "normal_low", nullable = false, precision = 5, scale = 2)
    public BigDecimal normalLow;

    @Column(name = "normal_high", nullable = false, precision = 5, scale = 2)
    public BigDecimal normalHigh;

    @Column(columnDefinition = "TEXT")
    public String description;
}
