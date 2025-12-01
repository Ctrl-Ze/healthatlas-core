package com.healthatlas.core.bloodtests.adapters.outbound.cache;

import com.healthatlas.core.bloodtests.domain.model.Analyte;
import com.healthatlas.core.bloodtests.ports.outbound.AnalyteLookupPort;
import com.healthatlas.core.bloodtests.ports.outbound.AnalyteRepositoryPort;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class AnalyteCacheAdapter implements AnalyteLookupPort {

    private final Map<String, Analyte> cache = new ConcurrentHashMap<>();
    private static final Logger LOG = Logger.getLogger(AnalyteCacheAdapter.class);

    @Inject
    AnalyteRepositoryPort analyteRepository;

    @PostConstruct
    void init() {
        reload();
    }

    @Override
    public Analyte getByName(String name) {
        Analyte a = cache.get(name.toUpperCase());
        if (a == null) {
            //TODO add domain exception
            throw new IllegalArgumentException("Unknown analyte: " + name);
        }
        return a;
    }

//TODO add an endpoint for admin to reload cache, maybe: /admin/cache/reload-analytes
    @Override
    public synchronized void reload() {
        LOG.info("Reloading analyte cache...");

        var analytes = analyteRepository.getAll();
        if (analytes.isEmpty()) {
            throw new IllegalStateException("No analytes found in DB. Ensure seed data is loaded.");
        }

        cache.clear();
        analytes.forEach(a -> cache.put(a.name.toUpperCase(), a));

        LOG.infof("Loaded %d analytes into cache", cache.size());
    }
}
