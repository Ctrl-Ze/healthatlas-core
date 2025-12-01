package com.healthatlas.core.bloodtests.adapters.outbound.cache;

import com.healthatlas.core.bloodtests.domain.model.Analyte;
import com.healthatlas.core.bloodtests.ports.outbound.AnalyteRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalyteCacheAdapterTest {

    AnalyteCacheAdapter cacheAdapter;
    AnalyteRepositoryPort repository;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(AnalyteRepositoryPort.class);

        cacheAdapter = new AnalyteCacheAdapter();
        cacheAdapter.analyteRepository = repository;
    }

    @Test
    void reload_shouldLoadAnalytesIntoCache() {
        // given
        Analyte hgb = new Analyte();
        hgb.id = UUID.randomUUID();
        hgb.name = "HGB";

        Analyte rbc = new Analyte();
        rbc.id = UUID.randomUUID();
        rbc.name = "RBC";

        when(repository.getAll()).thenReturn(List.of(hgb, rbc));

        // when
        cacheAdapter.reload();

        // then
        verify(repository, times(1)).getAll();
    }

    @Test
    void reload_shouldThrowWhenRepositoryEmpty() {
        // given
        when(repository.getAll()).thenReturn(List.of());

        // then
        assertThrows(IllegalStateException.class, () -> cacheAdapter.reload());
    }

    @Test
    void getByName_shouldThrowWhenNotFound() {
        // given
        Analyte hgb = new Analyte();
        hgb.name = "HGB";

        when(repository.getAll()).thenReturn(List.of(hgb));

        // when
        cacheAdapter.reload();

        // then
        assertThrows(IllegalArgumentException.class, () -> cacheAdapter.getByName("XYZ"));
    }

    @Test
    void getByName_shouldBeCaseInsensitive() {
        // given
        Analyte hgb = new Analyte();
        hgb.name = "Hgb";

        when(repository.getAll()).thenReturn(List.of(hgb));

        // when
        cacheAdapter.reload();

        // then
        assertSame(hgb, cacheAdapter.getByName("HGB"));
        assertSame(hgb, cacheAdapter.getByName("hGb"));
    }
}