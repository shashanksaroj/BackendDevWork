
---

🔹 STEP 2: Spring Boot Internals & Auto-Configuration


---

1️⃣ What really is @SpringBootApplication?

@SpringBootApplication
public class DemoApplication { }

Internally it is 👇

@Configuration
@EnableAutoConfiguration
@ComponentScan

What each does

Annotation	Purpose

@Configuration	Marks config class
@ComponentScan	Finds beans
@EnableAutoConfiguration	Magic happens here,annotation enables the auto-configuration of Spring ApplicationContext by scanning the classpath components and registering the beans.



---

2️⃣ How Spring Boot Auto-Configuration Works (CORE QUESTION)

Flow (VERY IMPORTANT)

1. App starts


2. Reads classpath


3. Reads application.yml


4. Applies conditions


5. Creates beans automatically



Example:

If spring-web on classpath → DispatcherServlet created

If spring-data-jpa present → EntityManagerFactory


🔥 Key idea

> Spring Boot is conditional configuration, not magic.




---

3️⃣ Conditional Annotations (You MUST know)

Annotation	Meaning

@ConditionalOnClass	If class exists
@ConditionalOnMissingBean	If bean NOT defined
@ConditionalOnProperty	If property exists
@ConditionalOnBean	If bean exists


Interview example

> ❓ How does Spring Boot avoid creating duplicate beans?



Answer @ConditionalOnMissingBean


---

4️⃣ Starter Dependencies (Hidden Internals)

Example:

spring-boot-starter-web

Includes:

Spring MVC

Jackson

Embedded Tomcat

Validation


> ❓ Why starters are preferred?



Answer

Version compatibility

Reduced dependency hell



---

5️⃣ SpringApplication.run() Internals

Spring Boot startup

What happens in order

1. Environment prepared


2. ApplicationContext created


3. Beans loaded


4. Auto-config applied


5. App ready




---

6️⃣ application.yml vs application.properties

Feature	YAML	Properties

Readability	✅	❌
Hierarchy	✅	❌
Preferred	✅	⚠️


Property precedence (VERY IMPORTANT)

1. Command-line args


2. Env variables


3. application.yml


4. application.properties




---

7️⃣ Mini Task (DO THIS)

✅ Task 1 – Disable Auto-Config

@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class
})

👉 Use when:

No DB

Custom DB config



---

✅ Task 2 – Conditional Bean

@Configuration
public class FeatureConfig {

    @Bean
    @ConditionalOnProperty(
        name = "feature.payment.enabled",
        havingValue = "true"
    )
    public PaymentService paymentService() {
        return new PaymentService();
    }
}

feature:
  payment:
    enabled: true


---

8️⃣ Real Interview Questions (MEMORIZE)

1. How does Spring Boot decide which beans to create?


2. What happens if two auto-configs create same bean?


3. Difference between Spring & Spring Boot?


4. How do you disable auto-configuration?


5. What is classpath scanning?




---

🔥 SDE-2 GOLD STATEMENT

> “Spring Boot relies on conditional auto-configuration based on classpath and properties, not runtime reflection.”

to change server to jetty, add dependency i pom.xml


---




---
