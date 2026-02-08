
---

✅ STEP 1 — TASK 1

PaymentService → OrderService → OrderController
With bean lifecycle logs


---

📁 package structure (important for interviews)

com.example.demo
 ├── controller
 ├── service
 ├── repository


---

1️⃣ Repository Layer

package com.example.demo.repository;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    public void saveOrder() {
        System.out.println("Order saved in DB");
    }
}

👉 @Repository is important for exception translation


---

2️⃣ Service Layer – PaymentService

package com.example.demo.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @PostConstruct
    public void init() {
        System.out.println("PaymentService bean created");
    }

    public void processPayment() {
        System.out.println("Payment processed");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("PaymentService bean destroyed");
    }
}


---

3️⃣ Service Layer – OrderService (Constructor Injection)

package com.example.demo.service;

import com.example.demo.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;

    public OrderService(PaymentService paymentService,
                        OrderRepository orderRepository) {
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    public void init() {
        System.out.println("OrderService bean created");
    }

    public void placeOrder() {
        paymentService.processPayment();
        orderRepository.saveOrder();
        System.out.println("Order placed successfully");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("OrderService bean destroyed");
    }
}

🔥 Interview point
If any dependency is missing → app fails at startup (fail-fast).


---

4️⃣ Controller Layer

package com.example.demo.controller;

import com.example.demo.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order")
    public String createOrder() {
        orderService.placeOrder();
        return "Order Created";
    }
}


---

✅ STEP 1 — TASK 2

Two beans of same type → @Qualifier & @Primary


---

5️⃣ Interface

package com.example.demo.service;

public interface NotificationService {
    void notifyUser();
}


---

6️⃣ EmailNotificationService (@Primary)

package com.example.demo.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class EmailNotificationService implements NotificationService {

    @Override
    public void notifyUser() {
        System.out.println("Email notification sent");
    }
}


---

7️⃣ SmsNotificationService (@Qualifier)

package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service("smsNotification")
public class SmsNotificationService implements NotificationService {

    @Override
    public void notifyUser() {
        System.out.println("SMS notification sent");
    }
}


---

8️⃣ Inject Using @Qualifier

package com.example.demo.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    private final NotificationService notificationService;

    public AlertService(
        @Qualifier("smsNotification") NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    public void sendAlert() {
        notificationService.notifyUser();
    }
}


---

🔥 Interview Questions YOU CAN NOW ANSWER

✅ Why constructor injection is preferred
✅ What happens if two beans of same type exist
✅ Difference between @Primary & @Qualifier
✅ When @PostConstruct runs
✅ Why @Repository matters


---
