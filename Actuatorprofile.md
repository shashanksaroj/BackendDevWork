

---

🔧 Spring Boot Actuator (1-page brain dump)

What is Actuator?

➡️ Production insights for your app
➡️ Health, metrics, env, beans, threads, HTTP stats

Think: “Doctor report for Spring Boot”


---

Core Actuator Endpoints (remember H-M-E-B-T)

Endpoint	Purpose

/actuator/health	App alive or dying
/actuator/metrics	CPU, memory, HTTP
/actuator/env	Env + config
/actuator/beans	All Spring beans
/actuator/threaddump	Thread issues


👉 Interview default question
Q: Is actuator enabled by default?
A: YES (but endpoints are secured)


---

Enable Actuator

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>


---

Expose Endpoints (prod must-know)

management.endpoints.web.exposure.include=health,info,metrics

⚠️ Never expose env, beans, dump in prod


---

Health Details

management.endpoint.health.show-details=always

Health states:

UP

DOWN

OUT_OF_SERVICE



---

Custom Health Check (interview GOLD)

@Component
class DbHealth implements HealthIndicator {
  public Health health() {
    return Health.up().withDetail("db", "connected").build();
  }
}


---

Metrics Example

/actuator/metrics/http.server.requests

Used by:

Prometheus

Grafana

Datadog



---

Security Rule (VERY IMPORTANT)

management.endpoints.web.base-path=/actuator

👉 Secure actuator separately (role: ACTUATOR_ADMIN)


---

Actuator = Used By

Kubernetes liveness probe

Auto scaling

Alerting

Debug prod crashes



---

🧠 One-Line Actuator Memory Trick

> Actuator = “Spring ka ICU monitor”




---


---

🎭 Spring Profiles (super crisp)

What is a Profile?

➡️ Environment-specific config

Examples:

dev

test

prod



---

Activate Profile

spring.profiles.active=dev

OR (prod way)

-Dspring.profiles.active=prod


---

Profile Config Files

application-dev.yml
application-test.yml
application-prod.yml


---

@Profile Annotation

@Profile("dev")
@Component
class DevBean {}

👉 Bean loads ONLY in dev


---

Multiple Profiles

spring.profiles.active=dev,local


---

@Profile vs @Conditional (interview)

@Profile	@Conditional

Simple env switch	Complex logic
Env based	Property/class based



---

Profile Priority Order (REMEMBER)

1️⃣ Command line
2️⃣ JVM args
3️⃣ Env vars
4️⃣ application.yml


---

Common Prod Setup

spring.profiles.active=prod
logging.level.root=INFO
spring.jpa.show-sql=false


---

🧠 One-Line Profile Memory Trick

> Profile = “Different masks for same app”




---

🚀 Interview Rapid-Fire Q&A

Q: Actuator vs Logs?
➡️ Logs = history
➡️ Actuator = live status

Q: Profiles at runtime?
➡️ ❌ No (startup only)

Q: Why Actuator blocked in prod?
➡️ Security risk


---

✅ FINAL CHEAT SUMMARY

Actuator = Monitoring
Profile   = Environment

