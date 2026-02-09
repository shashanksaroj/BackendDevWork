
---

🔥 Load Balancing in Spring Boot — COMPLETE NOTES


---

1️⃣ What is Load Balancing? (1-liner)

> Load Balancing = Distributing incoming requests across multiple service instances to improve availability, scalability, and fault tolerance.




---

2️⃣ Why Load Balancing is REQUIRED in Microservices

Multiple instances of same service

Dynamic scaling (pods/VMs)

Fault tolerance

Zero downtime deployments


❌ Hardcoded IP → single point of failure
✅ Load Balancer → resilient system


---

3️⃣ Types of Load Balancing (BIG PICTURE)

A️⃣ Client-Side Load Balancing

B️⃣ Server-Side Load Balancing


---

4️⃣ CLIENT-SIDE LOAD BALANCING (SPRING BOOT WAY)

📌 Definition

> Client decides which service instance to call



🧠 Mental Model

> “Smart client, dumb server”




---

🔹 Tools Used

Spring Cloud LoadBalancer

Netflix Eureka

OpenFeign

(Ribbon ❌ deprecated)



---

🔹 How It Works (TECH FLOW)

Service starts
   ↓
Registers to Eureka
   ↓
Client fetches registry (cached)
   ↓
Spring Cloud LoadBalancer picks instance
   ↓
HTTP call sent


---

🔹 Actual Spring Boot Code (Feign – Recommended)

@FeignClient(name = "user-service")
public interface UserClient {

  @GetMapping("/users/{id}")
  String getUser(@PathVariable("id") int id);
}

✔ No URL
✔ No @LoadBalanced
✔ Clean code


---

🔹 Load Balancing Happens Where?

Feign
 ↓
Spring Cloud LoadBalancer
 ↓
ServiceInstanceListSupplier
 ↓
Eureka Client Cache
 ↓
Instance chosen (Round Robin)


---

🔹 Default Algorithm

Round Robin



---

🔹 Custom Load Balancing Algorithm

@Bean
ReactorLoadBalancer<ServiceInstance> randomLB(
    Environment env,
    LoadBalancerClientFactory factory) {

  return new RandomLoadBalancer(
      factory.getLazyProvider(
          env.getProperty(LoadBalancerClientFactory.PROPERTY_NAME),
          ServiceInstanceListSupplier.class),
      env.getProperty(LoadBalancerClientFactory.PROPERTY_NAME)
  );
}


---

✅ Pros

No external infra needed

Fast (no extra hop)


❌ Cons

Client complexity

Language dependent

Not cloud-native



---

5️⃣ SERVER-SIDE LOAD BALANCING (MODERN PROD)

📌 Definition

> Client sends request to load balancer, which forwards to instances



🧠 Mental Model

> “Dumb client, smart infra”




---

🔹 Tools Used

Kubernetes

AWS ALB / NLB

NGINX

GCP Load Balancer



---

🔹 Kubernetes Load Balancing Flow

Client
 ↓
DNS (service-name)
 ↓
ClusterIP
 ↓
kube-proxy (iptables / IPVS)
 ↓
Pod

🔥 Spring Boot does NOT implement load balancing
🔥 Network layer handles it


---

🔹 Spring Boot Code (Nothing Special)

restTemplate.getForObject(
  "http://user-service/users/1",
  String.class
);

✔ No LoadBalancer
✔ No Eureka
✔ No Spring Cloud dependency


---

✅ Pros

Language agnostic

Cloud native

Production standard


❌ Cons

Infra dependency

Slight extra hop



---

6️⃣ Comparison Table (INTERVIEW GOLD)

Feature	Client-Side	Server-Side

Decision maker	Client	Load balancer
Spring dependency	Yes	No
Infra dependency	Low	High
Kubernetes	❌	✅
Modern prod	❌	✅



---

7️⃣ Algorithms Used in Spring Load Balancing

Client-Side (Spring Cloud LoadBalancer)

Round Robin (default)

Random

Custom (hash, weighted, etc.)


Server-Side

Round Robin

Least connections

IP hash

Latency-based



---

8️⃣ Common Interview Questions (With Answers)

❓ Does Feign always do load balancing?

❌ No

Eureka → Feign + Spring LB

Kubernetes → kube-proxy



---

❓ Is Eureka a load balancer?

❌ No
✔ Registry only


---

❓ Why Ribbon was removed?

Blocking

Netflix maintenance stopped

Not cloud-native



---

❓ Where is load balancing logic in Spring?

Spring Cloud LoadBalancer

LoadBalancerClient

ServiceInstanceListSupplier



---

9️⃣ When to Use What? (REAL LIFE)

Scenario	Use

Local dev	Eureka + Feign
Spring-only microservices	Client-side
Kubernetes	Server-side
Large scale prod	K8s + Service Mesh



---

🔟 One-Line Summary (MEMORIZE)

> Spring Boot supports client-side load balancing via Spring Cloud LoadBalancer and Feign, while modern production systems rely on server-side load balancing provided by platforms like Kubernetes and cloud load balancers.




---

If you want next notes:

✅ Service Discovery notes

✅ Feign deep dive

✅ Circuit breaker with load balancing

✅ Service Mesh (Istio) simplified

✅ End-to-end prod request flow


Just say which one 👊