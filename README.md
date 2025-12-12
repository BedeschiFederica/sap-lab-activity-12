#### Software Architecture and Platforms - a.y. 2025-2026

## Lab Activity #08 - 20251114  

v1.0.0-20251113


**Observability Patterns**
- **Health check API**
   - Instrumenting microservices with with an Health check API
        - exposing a REST `/health` endpoint, to get the health check status
        - status format example: [MicroProfile Health](https://github.com/eclipse/microprofile-health)
   - Who performs the health check?
		- exploting [Docker HEALTHCHECK](https://docs.docker.com/reference/dockerfile/#healthcheck)
		  - [healthcheck](https://docs.docker.com/reference/compose-file/services/#healthcheck) attribute in Docker Compose file
   - Reacting to unhealthy containers
        - unhealthy containers are **not** restarted by default (being health checks just diagnostic, not reactive)
          - there is not a single predefined approach to restart unhealthy containers
        - to restart containers that crashed: docker compose [restart policy](https://github.com/compose-spec/compose-spec/blob/main/spec.md#restart)
        - a way to restart unhealthy containers: [autoheal project](https://github.com/willfarrell/docker-autoheal)
   - Example: TTT Game Server
      - equipping `ttt-api-gateway` with a `/health` endpoint
      - `healthcheck` entry in `lab-activity-08/docker-compose.yml`
      - to check the healthy state of the service: `docker inspect api-gateway-01`
      - **NOTE:** the `autohealth` system is included in `lab-activity-08/docker-compose.yml`, but currently it is not working properly (for some reason to be understood)
- **Application metrics**
    - Instrumenting microservices with a metrics exporter 
    - Accessing data collected on the metrics service
    - [Prometheus platform](https://prometheus.io)
        - [concepts](https://prometheus.io/docs/introduction/overview/)
            - [architecture](https://prometheus.io/docs/introduction/overview/#architecture)
            - [data model](https://prometheus.io/docs/concepts/data_model/#data-model)
            - [metric and label naming](https://prometheus.io/docs/practices/naming/)
            - [getting started](https://prometheus.io/docs/prometheus/latest/getting_started/)
        - metrics service side
            - [installation](https://prometheus.io/docs/prometheus/latest/installation/) 
            - [configuration](https://prometheus.io/docs/prometheus/latest/configuration/configuration/)
            - [querying](https://prometheus.io/docs/prometheus/latest/querying/basics/)  
        - instrumentation side
            - [instrumentation](https://prometheus.io/docs/instrumenting/clientlibs/)
            - [Java client library](https://github.com/prometheus/client_java)
            - [Quickstart](https://prometheus.github.io/client_java/getting-started/quickstart/)
        - [API for applications/agents to query data](https://prometheus.io/docs/prometheus/latest/querying/api/)
        - [Grafana](https://grafana.com/) for building dashboards
    - Example: TTT Game Server
      - making `ttt-game-service` observable
        - two application level metrics
          - total number of games created (*count type*)
          - number of ongoing games (*gauge type*)
        - metrics server for Prometheus running on port `9400`, accessible at `http://localhost:9400/metrics`
      - making `ttt-api-gateway` observable
        - one infrastructure level metrics
          - total number of REST requests received (*count type*)
        - metrics server for Prometheus running on port `9401`, accessible at `http://localhost:9401/metrics`
      - Running a containerised Prometheus, port `9090` 
        - included in `lab-activity-08/docker-compose.yml` file
        - configured with two monitoring jobs (see `lab-activity-08/Prometheus.yml`) 
      - Prometheus dashboard is accessible at `http://localhost:9090`
      

		
		
		 




 





  

