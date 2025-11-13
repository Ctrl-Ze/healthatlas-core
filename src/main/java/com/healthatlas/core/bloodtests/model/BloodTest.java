package com.healthatlas.core.bloodtests.model;

import com.healthatlas.core.bloodtests.dto.request.BloodTestDto;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "blood_tests")
public class BloodTest extends PanacheEntityBase {

    //TODO Add versionNumber & previousVersionId (for versioning workflow)
    //TODO Add confirmedBy (doctor/user who confirmed)
    // TODO: add validation annotations later
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    // Reference to Cerberus user (not a real FK)
    @Column(name = "user_ref", nullable = false)
    public UUID userRef;

    //TODO maybe make it false
    @Column(nullable = false)
    public Instant timestamp;

    public boolean confirmed = false;

    @Column(name = "created_at", updatable = false, nullable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    public BloodTest() {
    }

    public BloodTest(UUID userRef, Instant timestamp, boolean confirmed) {
        this.userRef = userRef;
        this.timestamp = timestamp;
        this.confirmed = confirmed;
    }

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }

    public static BloodTest of(BloodTestDto dto, UUID userRef) {
        return new BloodTest(userRef, dto.timestamp, dto.confirmed);
    }
}