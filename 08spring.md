


---

🔹 STEP 6: Transactions in Spring Boot (Deep Dive)


---

1️⃣ What is a Transaction? (Say this clearly)

> “A transaction is a unit of work that must be all-or-nothing.”



Spring transactions are implemented using AOP proxies, not magic.


---

2️⃣ @Transactional — WHERE & WHY

✅ Correct place

@Service
@Transactional
public class OrderService {
    ...
}

❌ Wrong places

Controller ❌

Repository ❌


🔥 Interview one-liner:

> “Transaction boundaries belong in the service layer because business logic defines consistency.”




---

3️⃣ How Spring Transactions ACTUALLY work

Spring Framework

Behind the scenes

1. Spring creates a proxy


2. Proxy opens transaction


3. Calls your method


4. Commit → success


5. Rollback → exception



⚠️ IMPORTANT Calling a @Transactional method inside the same class
👉 ❌ Transaction will NOT start (proxy bypassed)


---

4️⃣ Rollback Rules (INTERVIEW FAVORITE)

Default behavior

Exception	Rollback?

RuntimeException	✅
Error	✅
Checked Exception	❌


Custom rollback

@Transactional(rollbackFor = Exception.class)

🔥 Interview trap:

> ❓ Why checked exceptions don’t rollback by default?



Answer

> Spring assumes checked exceptions are recoverable.




---

5️⃣ Transaction Propagation (VERY IMPORTANT)

Propagation = how transactions behave when methods call each other

Common types (you MUST know 3)

Propagation	Meaning

REQUIRED	Join or create (default)
REQUIRES_NEW	Suspend & create new
NESTED	Savepoint



---

🟢 REQUIRED (DEFAULT)

@Transactional
public void placeOrder() {
    payment();
}

Uses same transaction

If payment fails → order rolls back



---

🔴 REQUIRES_NEW (PROD USE CASE)

@Transactional(propagation = Propagation.REQUIRES_NEW)
public void auditLog() {
    // save audit record
}

🔥 Use case:

Audit logs

Metrics

Notifications


Even if main tx fails → audit still saved.


---

🟡 NESTED (SAVEPOINT)

@Transactional(propagation = Propagation.NESTED)

Partial rollback

Requires DB support



---

6️⃣ Isolation Levels (DB CONSISTENCY)

Isolation = how visible data is between transactions

Level	Prevents

READ_UNCOMMITTED	Nothing
READ_COMMITTED	Dirty reads
REPEATABLE_READ	Non-repeatable reads
SERIALIZABLE	All anomalies


Example

@Transactional(isolation = Isolation.READ_COMMITTED)

🔥 Interview line:

> “Higher isolation increases consistency but reduces concurrency.”




---

7️⃣ REAL PRODUCTION SCENARIO (VERY IMPORTANT)

Problem

Order saved

Payment failed

Inventory updated incorrectly ❌


Correct design

@Transactional
public void placeOrder() {
    saveOrder();
    chargePayment();   // throws RuntimeException
    updateInventory();
}

👉 Payment failure → everything rolls back


---

8️⃣ save(), flush(), commit (TRICK ZONE)

Action	Hits DB?

save()	❌
flush()	✅
commit	✅


🔥 Hibernate writes to DB at:

flush

commit

before query execution



---

9️⃣ COMMON INTERVIEW QUESTIONS (STEP 6)

❓ What happens if DB goes down mid-transaction?

Answer

Transaction rolls back

Connection returned to pool



---

❓ Can we have multiple transactions in one method?

Answer

Yes, using REQUIRES_NEW



---

❓ Why transactions fail sometimes even with @Transactional?

Answer

Self-invocation

Checked exception

Wrong proxy mode



---

🧠 SDE-2 TRANSACTION RULES (MEMORIZE)

✔ Service layer only
✔ RuntimeException → rollback
✔ REQUIRED vs REQUIRES_NEW
✔ Isolation is DB concern
✔ Beware self-invocation


---

✅ YOU ARE DONE WITH STEP 6 WHEN

You can explain propagation without code

You know why rollback didn’t happen

You’ve heard of self-invocation issue



---

