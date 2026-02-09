

1️⃣ Client-side discovery (Eureka)
2️⃣ Server-side discovery (Kubernetes – real prod)


---

1️⃣ CLIENT-SIDE SERVICE DISCOVERY (EUREKA) — ACTUAL CODE

A. Eureka Server (Service Registry)

📁 pom.xml

<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>

📁 EurekaServerApplication.java

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(EurekaServerApplication.class, args);
  }
}

📁 application.yml

server:
  port: 8761

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false

👉 This app is JUST a registry
👉 No business logic


---

B. User Service (Provider)

📁 pom.xml

<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

📁 application.yml

spring:
  application:
    name: user-service

server:
  port: 8081

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka

📁 UserController.java

@RestController
@RequestMapping("/users")
public class UserController {

  @GetMapping("/{id}")
  public String getUser(@PathVariable int id) {
    return "User " + id;
  }
}

👉 On startup, this service AUTO-REGISTERS to Eureka
👉 No manual code for registration


---

C. Order Service (Consumer)

📁 pom.xml

<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>


---

📁 OrderServiceApplication.java

@SpringBootApplication
public class OrderServiceApplication {

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  public static void main(String[] args) {
    SpringApplication.run(OrderServiceApplication.class, args);
  }
}

🔥 THIS @LoadBalanced IS THE MAGIC


---

📁 OrderController.java

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final RestTemplate restTemplate;

  public OrderController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping("/{id}")
  public String getOrder(@PathVariable int id) {

    String user =
        restTemplate.getForObject(
            "http://user-service/users/1",
            String.class
        );

    return "Order " + id + " for " + user;
  }
}


---

🚨 WHAT ACTUALLY HAPPENS HERE (TECHNICALLLY)

"http://user-service/users/1"
        ↓
@LoadBalanced intercepts
        ↓
Spring Cloud LoadBalancer
        ↓
EurekaClient (cached instances)
        ↓
Pick one instance
        ↓
Rewrite URL to IP:PORT
        ↓
Actual HTTP call

🔥 NO Eureka call per request


---

2️⃣ SERVER-SIDE SERVICE DISCOVERY (KUBERNETES) — ACTUAL CODE

👉 This is REAL production standard


---

A. Spring Boot Service (NO DISCOVERY CODE)

📁 UserController.java

@RestController
@RequestMapping("/users")
public class UserController {

  @GetMapping("/{id}")
  public String getUser(@PathVariable int id) {
    return "User " + id;
  }
}

❌ No Eureka
❌ No @LoadBalanced
❌ No Spring Cloud dependency


---

B. Kubernetes Deployment

📁 user-deployment.yaml

apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  replicas: 2
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
        - name: user-service
          image: user-service:latest
          ports:
            - containerPort: 8080


---

C. Kubernetes Service (THIS IS DISCOVERY)

📁 user-service.yaml

apiVersion: v1
kind: Service
metadata:
  name: user-service
spec:
  selector:
    app: user-service
  ports:
    - port: 80
      targetPort: 8080


---

D. Order Service Calling User Service

📁 OrderController.java

@RestController
@RequestMapping("/orders")
public class OrderController {

  @GetMapping("/{id}")
  public String getOrder(@PathVariable int id) {

    RestTemplate restTemplate = new RestTemplate();

    String user =
        restTemplate.getForObject(
            "http://user-service/users/1",
            String.class
        );

    return "Order " + id + " for " + user;
  }
}


---

🚨 WHAT HAPPENS NOW (IMPORTANT)

user-service
   ↓
Kubernetes DNS
   ↓
ClusterIP
   ↓
kube-proxy (iptables / IPVS)
   ↓
One pod selected
   ↓
Request forwarded

🔥 Spring Boot is CLUELESS
🔥 Network layer handles everything


---

3️⃣ SIDE-BY-SIDE CODE COMPARISON

Feature	Eureka	Kubernetes

Registry	Eureka Server	K8s API
Discovery	Java client	DNS
Load balancing	Spring	kube-proxy
App code	More	Almost zero
Prod ready	❌	✅



---

4️⃣ INTERVIEW FINAL ANSWER (MEMORIZE)

> In Eureka, service discovery is implemented using
@LoadBalanced RestTemplate which intercepts service names
and resolves them via Eureka registry.

In Kubernetes, service discovery is implemented via DNS
and kube-proxy at the network layer, so Spring Boot needs no discovery code.




---

If you want next (very useful):

✅ Feign client version (cleaner code)

✅ Spring Cloud LoadBalancer internal classes

✅ Why Eureka fails in k8s

✅ Service Mesh (Istio) code flow


Just say the word 👊