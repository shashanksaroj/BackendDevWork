
---

✅ STEP 2 — TASK 1

Disable Spring Boot Auto-Configuration

🎯 Use case

App has no database

Or you want custom DB config

Or testing without DB



---

1️⃣ Main Application Class

package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
    exclude = { DataSourceAutoConfiguration.class }
)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

🔥 Interview Explanation

Spring Boot won’t try to create a DataSource

Prevents startup failure when DB config is missing


❓ Interview Question

> When would you exclude auto-configuration?



Answer

Microservice without DB

Custom multi-datasource setup

Lightweight services



---

✅ STEP 2 — TASK 2

Conditional Bean Creation (@ConditionalOnProperty)


---

2️⃣ PaymentService (Simple POJO)

package com.example.demo.service;

public class PaymentService {

    public void pay() {
        System.out.println("Payment feature enabled");
    }
}

⚠️ Notice:
No @Service annotation — Spring should NOT auto-create it.


---

3️⃣ Configuration Class (Core of Auto-Config)

package com.example.demo.config;

import com.example.demo.service.PaymentService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentFeatureConfig {

    @Bean
    @ConditionalOnProperty(
        name = "feature.payment.enabled",
        havingValue = "true"
    )
    public PaymentService paymentService() {
        return new PaymentService();
    }
}

🔥 What Spring Does Internally

Reads application.yml

Checks property

Creates bean only if condition matches



---

4️⃣ application.yml

feature:
  payment:
    enabled: true

🔁 Change to false → bean NOT created


---

5️⃣ Controller to Verify Behavior

package com.example.demo.controller;

import com.example.demo.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final PaymentService paymentService;

    public TestController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/pay")
    public String pay() {
        paymentService.pay();
        return "OK";
    }
}

❌ If enabled=false

App will fail at startup:

NoSuchBeanDefinitionException

🔥 THIS is fail-fast design


---

🧠 BONUS: Interview-Grade Explanation

❓ Why @ConditionalOnProperty is used in Spring Boot?

Answer

Feature toggles

Environment-specific beans

Microservice customization


❓ How Spring Boot avoids duplicate beans?

Answer

@ConditionalOnMissingBean



---

✅ You Have MASTERED STEP 2 If

✔ You can disable auto-config
✔ You understand conditional beans
✔ You can explain Spring Boot magic logically


---
