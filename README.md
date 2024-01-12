### 📖 Dealing with Concurrency in Saga Transactions for Spring Boot Microservices

#### ✅ Event Sourcing CQRS Concurrency Saga with Axon Event Store
#### ✅ E2E Testing Framework with Spring Cloud OpenFeign

<ul style="list-style-type:disc">
    <li>📖 This <b>Stock Tracking Platform with Axon Event Store</b> provides fully functional Development Environment:</li>
    <li>📖 <b>Event-Driven Spring Boot Microservices</b> with Axon Event Sourcing and Saga Framework</li>
    <li>📖 <b>Swagger UI Gateway</b> with Keycloak Authorization</li>
    <li>📖 <b>E2E Testing Service</b> with Spring Cloud OpenFeign REST Client</li>
    <li>📖 Local <b>Docker</b> Development Environment</li>
  <li>📖 Full <b>Technology Stack</b>:</li>
  <ul>
    <li>✅ <b>Swagger UI</b></li>
    <li>✅ <b>Spring Cloud OpenFeign</b></li>
    <li>✅ <b>Spring Boot</b></li>
    <li>✅ <b>Spring Cloud Gateway</b></li>
    <li>✅ <b>Event-Driven Microservices</b></li>
    <li>✅ <b>Axon Event Sourcing</b></li>
    <li>✅ <b>Axon CQRS</b></li>
    <li>✅ <b>Axon Saga Transactions</b></li>
    <li>✅ <b>Axon Event Streaming</b></li>
    <li>✅ <b>Axon Event Store</b></li>
    <li>✅ <b>Axon Event Monitoring Console</b></li>
    <li>✅ <b>CQRS Query Projection with PostgreSQL Database</b></li>
    <li>✅ <b>Keycloak Oauth2 Authorization Server</b></li>
    <li>✅ <b>Local Docker Environment</b></li>
    <li>✅ <b>E2E Testing Framework</b></li>
    <li>✅ <b>E2E Concurrency Testing with Multiple Threads</b></li>
    <li>✅ <b>Remote Debugging</b></li>
    <li>✅ <b>Zipkin Distributed Tracing</b></li>
  </ul>
</ul>

### 📖 Links

- [Axon Spring Boot Websocket Github Page by Ivan Franchin](https://github.com/ivangfr/axon-springboot-websocket)
- [Event-Driven Microservices, CQRS, SAGA, Axon, Spring Boot Udemy Course](https://www.udemy.com/course/spring-boot-microservices-cqrs-saga-axon-framework)
- [Stock Tracking Swagger UI with Axon Event Sourcing, CQRS and Saga Transactions](https://github.com/greeta-stock-01/stock-api)
- [E2E Testing Pipeline for Spring Boot Microservices using OpenFeign Client and Github Actions](https://www.linkedin.com/pulse/e2e-testing-pipeline-spring-boot-microservices-using-openfeign/)

### 📖 Step By Step Guide

#### Local Docker Environment Setup:

```
sh docker-start.sh
```

- this script will build docker images and start environment with your code changes

- Warning! Make sure that Axon Server is initialized! (see `Axon Server Troubleshooting` below for more information)

```
sh docker-app-restart.sh order
```

- this script will rebuild spring boot docker image for `order` aplication and restart application with rebuilt image
- replace `order` with the name of the application you want to rebuild and restart


#### Local Docker Environment E2E Testing:

- make sure you started local docker environment with `sh docker-start.sh` command

- open http://localhost:9000 in your Browser and make sure that Swagger UI is working

- go to `e2e-service` and run all E2E Tests with this command: `mvn test -Dtest="*E2eTest"`

- alternatively, run each test separately in your IDE

- alternatively, you can run all E2E Tests as part of your Github Actions Pipeline against your cloud environment REST API

- for more details see: [E2E Testing Pipeline for Spring Boot Microservices using OpenFeign Client and Github Actions](https://www.linkedin.com/pulse/e2e-testing-pipeline-spring-boot-microservices-using-openfeign/)

- Use `Axon Server Console` to monitor Event Sourcing and Saga Transaction Events, related to `Approved` and `Rejected` Orders: http://localhost:8024/

- Use `PostgreSQL` Database Client (for example DBeaver) to monitor `orderdb` and `productdb` databases

- Warning! Before each E2E test, PostgreSQL database and Axon Event Store are completely reset: all data is clean before running each E2E Test

- For E2E REST API requests OpenFeign uses  `admin/admin` password credentials (it receives `access_token` from Keycloak Server using `password` grant type)

- Make sure that Keycloak Server is running as part of your Local Docker Environment: http://localhost:8080 (otherwise OpenFeign REST API authorized requests will not work)

- Warning! If Swagger UI fails to load on the first try, please, refresh the page!

- Warning! Sometimes switching between Swagger UI pages doesn't refresh Swagger UI completely and you might see wrong REST endpoints: just refresh the page and continue

- Warning! Sometimes Swagger UI REST endpoints return `504 Gateway Timeout`, just retry the REST API endpoint again

- For Swagger UI `POST` requests: click `Authorize` and use `admin/admin` or `user/user` for credentials (`clientId` should be `stock-app`)


### Remote Debugging

- use `BPL_DEBUG_PORT` property in `docker-app-compose.yml` for remote debugging port of the spring boot application (see `Port` in the screenshot)

- select spring boot application from the dropdown list (see `Use module classpath` in the screenshot)

![Configuration to debug a containerized Java application from IntelliJ IDEA](documentation/06-14.png)

#### Axon Server Console

- Use `Axon Server Console` for `Event Monitoring and Tracing`: http://localhost:8024/


#### Axon Server Troubleshooting

- When you run `Axon Server` for the first time:
- Manually create folders `volumes/config`, `volumes/data`, `volumes/events`, `volumes/plugins`
- Go to Axon Server Console: http://localhost:8024/
- Click `Finish` button in the setup configuration dialog

#### Zipkin Server

- Zipkin Server for Distributed Tracing is available here: http://localhost:9411/
