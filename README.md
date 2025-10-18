# healthatlas-core (Athena)

**Codename:** Athena — *the brain of HealthAtlas.*

Athena is the central aggregation and orchestration service in the HealthAtlas ecosystem.  
It coordinates and normalizes data from multiple domains (auth, ingestion, OCR, analytics), and exposes unified APIs to the frontend (Helios).

---

## Responsibilities
- Aggregate and normalize health data across subsystems
- Provide a central API gateway and validation layer
- Orchestrate microservice communication and consistency
- Act as the single source of truth for system-wide entities

---

## Related Services

TODO: Think more about this

| Service | Codename | Description |
|----------|-----------|-------------|
| healthatlas-auth | Cerberus | Authentication & authorization |
| healthatlas-ocr | Hermes | OCR and document extraction |
| healthatlas-ingest | Iris / Pan | Wearable ingestion |
| healthatlas-analytics | Themis | Analytics and insight detection |
| healthatlas-audit | Mnemosyne | Audit and memory logs |
| healthatlas-notify | Echo | Alerts and notifications |
| healthatlas-ai | Chiron | AI and guidance layer |
| healthatlas-web | Helios | Frontend dashboard |

---

## Local Development

Athena runs in Docker using **Gradle** and **Quarkus**:

```bash
./gradlew build -x test
docker-compose up --build
