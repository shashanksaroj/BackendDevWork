


---

1️⃣ What is Service Discovery? (In simple words)

> Service Discovery = “How does one service find another service’s IP & port dynamically?”



In microservices:

Services scale up/down

Pods/instances die & restart

IPs and ports change constantly


❌ Hard-coding URLs = 💀 production nightmare
✅ Service Discovery solves this


---

2️⃣ Core Components of Service Discovery

🧩 1. Service Registry

A central place that knows:

Service name

Instance IP

Port

Health status


Examples:

Netflix Eureka

Consul

Zookeeper

Kubernetes (built-in)


🧩 2. Service Provider

Registers itself

Sends heartbeats

Deregisters on shutdown


🧩 3. Service Consumer

Asks registry: “Where is user-service?”

Calls one of the instances



---

3️⃣ Service Discovery Flow (Visual)

Flow:

1. User-Service starts


2. Registers with registry


3. Order-Service asks registry


4. Registry returns list of instances


5. Request is routed




---

4️⃣ Two Types of Service Discovery (🔥 Important)

A️⃣ Client-Side Service Discovery

📌 Definition

> Client itself decides which service instance to call



🧠 Mental Model

> “Client is smart”



🔁 Flow

1. Client calls registry


2. Gets all instances


3. Chooses one (load balancing)


4. Calls service directly



🛠 Used With

Netflix Eureka

Ribbon (older)

Spring Cloud LoadBalancer


✅ Pros

No extra hop

Faster

Simple infra


❌ Cons

Client becomes complex

Tight coupling

Harder for non-Java clients


🧪 Spring Boot Example

@LoadBalanced
@Bean
RestTemplate restTemplate() {
    return new RestTemplate();
}

restTemplate.getForObject(
  "http://USER-SERVICE/users/1", User.class);

👉 Eureka resolves USER-SERVICE


---

B️⃣ Server-Side Service Discovery

📌 Definition

> Client sends request to a load balancer, not directly to service



🧠 Mental Model

> “Client is dumb, server is smart”



🔁 Flow

1. Client → Load Balancer


2. Load Balancer queries registry


3. Routes request



🛠 Used With

Kubernetes

AWS ALB / NLB

NGINX

GCP Load Balancer


✅ Pros

Client simple

Language-agnostic

Enterprise-friendly


❌ Cons

Extra network hop

Infra complexity


🧪 Kubernetes Example

http://user-service/users/1

K8s Service handles:

Discovery

Load balancing

Health checks



---

5️⃣ Spring Boot + Service Discovery Options (Interview Gold)

Environment	Discovery Type	Tool

Local / Dev	Client-Side	Eureka
Spring Cloud	Client-Side	Spring Cloud LoadBalancer
Kubernetes	Server-Side	K8s Service
AWS	Server-Side	ALB + Target Groups
GCP	Server-Side	Cloud Load Balancer



---

6️⃣ Eureka in Spring Boot (Quick Setup)

1️⃣ Eureka Server

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApp {}

2️⃣ Eureka Client

spring:
  application:
    name: user-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka


---

7️⃣ Client-Side vs Server-Side (Final Comparison)

Feature	Client-Side	Server-Side

Load balancing	Client	Load balancer
Client complexity	High	Low
Infra dependency	Low	High
Cloud native	❌	✅
Kubernetes	❌	✅



---

8️⃣ Interview One-Liners (🔥 Memorize)

Service Discovery → Dynamic lookup of service instances

Client-Side Discovery → Client chooses instance

Server-Side Discovery → Load balancer chooses instance

Eureka → Registry, not load balancer

Kubernetes Service → Registry + LB together



---

9️⃣ Which is used in REAL PRODUCTION?

✅ Modern Production

Kubernetes

Server-side discovery

Cloud Load Balancers


❌ Eureka

Mostly legacy Spring Cloud systems

Not cloud-native



---

If you want next:

✅ Eureka vs Kubernetes (deep)

✅ Spring Cloud LoadBalancer vs Ribbon

✅ How Netflix removed Eureka

✅ Service Discovery interview Q&A (SDE-2 level)


Just tell me 👊