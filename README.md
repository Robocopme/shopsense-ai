# ShopSense AI

ShopSense AI is a production-ready shopping intelligence platform that combines secure data ingestion, resilient microservices, realtime analytics, and on-device legal-compliant data capture. This repository packages the full stack: eight Spring Boot services, a Spring Cloud Gateway, infrastructure-as-code, observability defaults, and a Manifest V3 browser extension with animated insights UI.

## Repository Layout
- `services/` — Contains `api-gateway` plus the seven domain services (user, product, price, review, AI insights, notification, analytics) each with DTOs, mappers, persistence, OpenAPI, tracing, and compliance-aware configs.
- `shared-kernel` — Shared DTOs, JWT utilities, error primitives, and domain events.
- `browser-extension` — Manifest V3 extension with Amazon/Walmart/Best Buy extractors and animated sidebar.
- `infrastructure` — Advanced Docker assets (full observability stack, Nginx CDN tier) for production parity.
- `docs` — Architecture diagram, legal/compliance checklist, and operational notes.
- `scripts/deploy.sh` — One-command build and compose bootstrap.

## Microservices
| Service | Port | Responsibilities |
| --- | --- | --- |
| API Gateway | 8080 | SSL termination, JWT auth, Redis rate limiting, route orchestration, circuit breakers |
| User Service | 8081 | Registration, auth, consent ledger, refresh tokens, preference storage |
| Product Service | 8082 | Catalog ingestion, retailer API orchestration, Elasticsearch indexing |
| Price Tracking Service | 8083 | Scheduled price polls, threshold detection, Kafka price-events |
| Review Analysis Service | 8084 | Fake review scoring, sentiment pipeline, audit storage |
| AI Insights Service | 8085 | Context aggregation, Ollama prompt building, caching |
| Notification Service | 8086 | Email/Push fan-out, GDPR-compliant opt-outs, Kafka consumers |
| Analytics Service | 8087 | Usage metering, business KPIs, BI exports |

Every service exposes OpenAPI 3 docs (`/v3/api-docs` + Swagger UI), ships with health checks (`/actuator/health`), metrics (Prometheus format), distributed tracing headers (W3C Trace Context), structured logging (correlation IDs), and security hardening (CORS, CSRF disabled, JWT validation, least-privilege endpoints).

## Infrastructure
- Single `docker-compose.yml` now drives every environment: the default profile brings up Postgres, Redis, Kafka + Zookeeper plus a `kafka-init` helper (auto-creates required topics), Kafka UI, Elasticsearch, Ollama, MailHog, and all eight Spring Boot services.
- Optional profiles unlock heavy-weight components:
  - `edge` — adds the Nginx reverse proxy that terminates TLS before the gateway.
  - `observability` — enables Prometheus, Grafana, Tempo, and Kibana for tracing & metrics.
- Example: `docker compose --profile edge --profile observability up -d` spins the entire production-like topology; omit profiles for a lighter local loop.
- Compliance: brokered secrets via env vars, encrypted volumes, GDPR/CCPA retention defaults defined in `docs/legal-compliance.md`.

## Getting Started
1. Install JDK 21, Docker 24+, Node 20+ (for browser extension builds) and GNU Make/bash.
2. Export the secrets you need (tokens, SMTP creds, retailer API keys). Sample defaults live in `docker-compose.yml` (see the `environment` sections).
3. Run `./scripts/deploy.sh` — this compiles all services (`./gradlew clean bootJar`) and then brings up the compose stack (add `DOCKER_PROFILES=\"edge,observability\"` before the command if you want to opt-in to optional services).
4. Browse Swagger at `https://localhost/swagger` (served via gateway aggregation) or per-service `http://localhost:808X/swagger-ui.html`.
5. Load the browser extension by visiting `chrome://extensions`, enabling Developer Mode, and selecting `browser-extension`.

## Browser Extension Highlights
- Manifest V3 service worker with granular host permissions; extraction only runs after explicit user interaction.
- Modular extractors for Amazon, Walmart, and Best Buy that rely solely on DOM data provided on the page.
- Animated sidebar with Tailwind-inspired styling, streaming AI insights via `EventSource` to `/api/v1/insights/analyze`.
- Privacy-first: no background scraping, explicit consent copy in UI, telemetry opt-out persisted via `chrome.storage`.

## Security & Compliance
- JWT (access + refresh) with rotating secrets, Redis-backed token revocation, device binding metadata.
- Redis rate limiting tiers (per-IP and per-plan) configured in gateway + services.
- Resilience patterns: retry w/ exponential backoff, circuit breakers, graceful fallbacks to cached data.
- Legal posture documented in `docs/legal-compliance.md` covering GDPR, CCPA, copyright, and retailer TOS alignment.

## API Documentation
- `springdoc-openapi` enabled across services with custom branding.
- Gateway aggregates service docs and republishes at `/swagger` for DX.
- Brownfield support: versioned `/api/v1` routes, compatibility headers, sample clients in the README.

## Next Steps
- Configure your CI pipeline to run `./gradlew test` plus `docker compose build` for smoke verification.
- Add retailer-specific credentials as Docker secrets or AWS Parameter Store entries referenced by the compose stack.
- Extend analytics schemas to your BI warehouse (Snowflake/BigQuery) via the provided Kafka topics.
