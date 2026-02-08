


---

🔹 STEP 5: JPA & Hibernate (Production-Grade)


---

1️⃣ What JPA & Hibernate REALLY are (say this)

JPA → Specification (rules)

Hibernate → Implementation


🔥 Interview one-liner:

> “JPA defines how ORM should behave, Hibernate actually implements it.”




---

2️⃣ Entity Lifecycle (VERY IMPORTANT)

States:

NEW → MANAGED → DETACHED → REMOVED

Example

User user = new User();          // NEW
entityManager.persist(user);    // MANAGED
entityManager.detach(user);     // DETACHED
entityManager.remove(user);     // REMOVED

❓ Interview question
When does Hibernate hit DB?
👉 At flush / transaction commit, not immediately.


---

3️⃣ Real Production Entities (User → Orders)

User Entity

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(
        mappedBy = "user",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    private List<Order> orders = new ArrayList<>();
}


---

Order Entity

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}

🔥 SDE-2 rule

> Always use LAZY unless proven otherwise.




---

4️⃣ Lazy vs Eager (INTERVIEW FAVORITE)

Fetch	Problem

EAGER	Loads too much data
LAZY	Can throw exception


Lazy loading failure

User user = userRepo.findById(1L).get();
user.getOrders().size(); // 💥 LazyInitializationException

Why?
👉 Session is closed.


---

5️⃣ N+1 Problem (BIG RED FLAG IF YOU DON’T KNOW)

Code

List<User> users = userRepo.findAll();
for (User u : users) {
    u.getOrders().size();
}

Queries executed

1 query → users
N queries → orders

🔥 Interview one-liner:

> “N+1 happens when Hibernate fires one query for parent and N queries for children.”




---

6️⃣ Fix N+1 (PRODUCTION SOLUTION)

✅ Using JOIN FETCH

@Query("""
   select u from User u
   join fetch u.orders
""")
List<User> findAllWithOrders();

OR

✅ EntityGraph

@EntityGraph(attributePaths = "orders")
List<User> findAll();


---

7️⃣ Transactions (ABSOLUTE MUST)

Spring Data JPA

Service Layer Transaction

@Service
public class OrderService {

    @Transactional
    public void placeOrder() {
        saveOrder();
        chargePayment();
        updateInventory();
    }
}

🔥 Interview line:

> “Transactions belong in service layer, not controller.”




---

8️⃣ Rollback Rules (IMPORTANT)

Default

Rollback on RuntimeException

❌ No rollback on checked exception


Custom rollback

@Transactional(rollbackFor = Exception.class)


---

9️⃣ save() vs saveAndFlush() (TRICK QUESTION)

Method	Behavior

save()	Persist later
saveAndFlush()	Immediate DB hit


🔥 Use saveAndFlush() when:

You need generated ID immediately

You want DB constraint validation early



---

🔥 COMMON INTERVIEW QUESTIONS (STEP 5)

❓ Why LAZY by default?

Answer

Performance

Avoid unnecessary joins



---

❓ Where does LazyInitializationException come from?

Answer

Accessing lazy field outside transaction/session



---

❓ How do you fix N+1?

Answer

JOIN FETCH

EntityGraph

Batch fetching



---

🧠 SDE-2 ORM RULES (MEMORIZE)

✔ Entities ≠ DTOs
✔ LAZY by default
✔ Transactions in service
✔ Never expose entities
✔ Always think query count


---

✅ YOU ARE DONE WITH STEP 5 WHEN

You can explain N+1 without code

You know when DB query executes

You instinctively use JOIN FETCH



---

