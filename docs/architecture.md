# ShopSense AI Architecture

```mermaid
graph TD
    subgraph Client Layer
        A[Browser Extension]
        B[React Web App]
        C[React Native (Future)]
    end
    subgraph Edge
        D[Nginx CDN/ALB]
        E[Spring Cloud Gateway]
    end
    subgraph Services
        F1[User Service 8081]
        F2[Product Service 8082]
        F3[Price Tracking 8083]
        F4[Review Analysis 8084]
        F5[AI Insights 8085]
        F6[Notification 8086]
        F7[Analytics 8087]
    end
    subgraph Data Layer
        G1[(PostgreSQL)]
        G2[(Redis)]
        G3[(Elasticsearch)]
        G4[(Kafka/MSK)]
        G5[(Ollama Llama3.1 8B)]
    end
    subgraph Observability
        H1[Prometheus]
        H2[Grafana]
        H3[Tempo/X-Ray]
    end
    A -->|JWT + HTTPS| D
    B -->|JWT + HTTPS| D
    D --> E
    E -->|Route + Rate Limit| F1
    E --> F2
    E --> F3
    E --> F4
    E --> F5
    E --> F6
    E --> F7
    F1 -->|user-events| G4
    F2 -->|product-events| G4
    F3 -->|price-events| G4
    F4 -->|review-events| G4
    F5 -->|insight-events| G4
    F6 -->|notification-events| G4
    F7 -->|analytics-events| G4
    F2 --> G3
    F3 --> G1
    F1 --> G1
    F5 --> G5
    F5 --> G2
    F6 --> G2
    H1 --> H2
    H3 --> H2
    F1 --> H1
    F2 --> H1
    F3 --> H1
    F4 --> H1
    F5 --> H1
    F6 --> H1
    F7 --> H1
```

## Data Flows
1. **Product submission** — Browser extension posts to `/api/v1/products`, gateway authenticates JWT, Product Service records metadata, syncs with retailer APIs, indexes Elasticsearch, and publishes `product-events` to Kafka for downstream services.
2. **AI insights** — Extension calls `/api/v1/insights/analyze`, the AI Insights Service hydrates context from Product/Price/Review/User services, builds structured prompts, invokes Ollama, normalizes the response, caches it in Redis for one hour, and responds to the client.
3. **Price monitoring** — Price Tracking Service schedules hourly polls, hits retailer APIs or crowdsourced updates, compares against target prices, emits `price-events` when thresholds trip, Notification Service fans out via SMTP/FCM after querying User Service for channel preferences.
4. **Analytics** — All services emit immutable events to Kafka; Analytics Service aggregates them into PostgreSQL for compliance-grade auditing and exposes dashboards/exports.

## Resilience & Observability
- Resilience4j circuit breakers guard retailer integrations, with cached fallbacks.
- Retry (exponential backoff) on transient network calls.
- Distributed tracing via W3C Trace Context headers propagated from the extension to downstream services.
- Prometheus scrapes `/actuator/prometheus`; Grafana dashboards and alert rules cover p95 latency, error budgets, Kafka lag, and Redis hit rates.

## Deployment Topology
- AWS ALB terminates TLS and forwards to an Auto Scaling Group/EKS nodes hosting the gateway and services.
- Data services leverage managed offerings (RDS, MSK, ElastiCache, OpenSearch) for multi-AZ durability.
- Ollama runs on a G5 GPU instance with autoscaling policies keyed to queue depth and GPU utilization.
