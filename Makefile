# Makefile for HealthAtlas Athena
# Usage:
#   make db-up      → start only Athena DB
#   make athena-up  → start Athena + DB
#   make down       → stop and remove all
#   make logs       → tail logs

DOCKER_COMPOSE := docker-compose -f docker-compose.yml

# Start only DB for local dev
db-up:
	@echo "💾 Starting Athena DB..."
	$(DOCKER_COMPOSE) up -d athena-db

# Start Athena and DB
athena-up:
	@echo "🧠 Starting Athena service and DB..."
	$(DOCKER_COMPOSE) up -d healthatlas-core athena-db

# Stop all containers
down:
	@echo "🛑 Stopping Athena containers..."
	$(DOCKER_COMPOSE) down

db:
	@echo "🗄️ Connecting to Athena DB in docker..."
	docker exec -it athena-db psql -U postgres -d healthatlas

# Tail logs
logs:
	@echo "📜 Tailing Athena logs..."
	$(DOCKER_COMPOSE) logs -f

# Cleanup volumes
clean:
	@echo "🧹 Removing Athena containers and volumes..."
	$(DOCKER_COMPOSE) down -v
