package com.healthatlas.core.bloodtests.cache;

import com.healthatlas.core.bloodtests.model.Analyte;
import com.healthatlas.core.bloodtests.repository.AnalyteRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class AnalyteCache {

    private final Map<String, Analyte> cache = new ConcurrentHashMap<>();
    private static final Logger LOG = Logger.getLogger(String.valueOf(AnalyteCache.class));

    @Inject
    AnalyteRepository analyteRepository;

    @PostConstruct
    void init() {
        LOG.info("Initializing analyte cache...");
        var analytes = analyteRepository.findAll().list();
        if (analytes.isEmpty()) {
            throw new IllegalStateException("No analytes found in DB. Ensure seed data is loaded.");
        }

        analytes.forEach(a -> cache.put(a.name.toUpperCase(), a));
        LOG.infof("Loaded %d analytes into cache", cache.size());
    }

    public Analyte get(String name) {
        Analyte a = cache.get(name.toUpperCase());
        if (a == null) {
            throw new IllegalArgumentException("Unknown analyte: " + name);
        }
        return a;
    }
//TODO add an endpoint for admin to reload cache, maybe: /admin/cache/reload-analytes
    public synchronized void reload() {
        LOG.info("Reloading analyte cache...");
        cache.clear();
        init();
    }
}
