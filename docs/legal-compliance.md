# Legal & Compliance Checklist

## GDPR & CCPA
- **Lawful basis**: User-initiated browser actions serve as explicit consent; additional consent banner stored with timestamp, geo, and browser fingerprint in User Service.
- **Data minimization**: Extension captures only fields displayed on the product page; background scraping is disabled. Sensitive data (PII) is hashed at rest.
- **Right to access/delete**: User Service exposes `/api/v1/users/{id}` deletion endpoint with cascading erasure triggers (Kafka `user-deleted` event for all services).
- **Data portability**: Analytics Service exports user-specific activity reports (JSON/CSV) upon verified request.
- **DPA & SCCs**: Templates included for retailer and notification vendors; configure per deployment.

## Copyright & Retailer Terms
- **User-initiated collection** only; DOM extraction happens after explicit click. No automated crawling, no circumvention of paywalls or CAPTCHAs.
- **Attribution**: Product Service stores canonical retailer links and enforces cache TTLs that respect partner policies.
- **Robots.txt respect**: Fallback scrapers use retailer-provided APIs where available; scraping occurs solely within the user's browser context.

## Security Controls
- **Encryption**: TLS 1.3 everywhere; PostgreSQL/Redis/Kafka use TLS when deployed in production. Secrets load from env vars or vaults, never committed.
- **Authentication**: JWT (access + refresh) with rotation, Redis-backed revocation, device binding metadata.
- **Authorization**: Gateway injects `X-User-Id` header; services enforce role-based access with Spring Security method-level checks.
- **Rate limiting**: Redis-based quotas per IP + per subscription tier, configured in gateway and AI service.
- **Audit logging**: Analytics Service stores immutable audit trails tagged with consent references.

## Data Retention & Residency
- **Retention**: Default 365 days for analytics, 90 days for raw reviews, configurable per tenant. Automated jobs purge expired data monthly.
- **Residency**: PostgreSQL schemas can be sharded per region; replication policies documented for EU/US split deployments.

## LLM Usage (Ollama)
- No sensitive PII leaves the VPC. AI Insights Service redacts emails/order numbers before sending prompts.
- Prompt/response pairs stored for 30 days for quality review, then deleted.
- Model weights hosted on dedicated GPU node with audit logging.
