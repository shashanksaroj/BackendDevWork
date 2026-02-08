/*
================================================================================
        SPRING BOOT JPA MASTER NOTES (SDE-2 / SDE-3)
================================================================================
TOPICS COVERED IN THIS SINGLE FILE:
✔ Hibernate Cache (1st vs 2nd vs Redis)
✔ @Transactional propagation (with REAL bugs)
✔ LazyInitializationException (deep + fixes)
✔ Spring Boot JPA Production Checklist

THIS FILE = INTERVIEW GOLD.
================================================================================
*/

/*
===============================================================================
SECTION 1: HIBERNATE CACHE (🔥 VERY IMPORTANT)
===============================================================================

WHY CACHE?
• Reduce DB calls
• Improve response time
• Avoid repeated queries
*/

/*
------------------------------------------------------------------------------
1️⃣ FIRST LEVEL CACHE (L1 CACHE)
------------------------------------------------------------------------------
• Default cache
• Session scoped
• One entity = one DB hit per session
• Enabled automatically
• Cannot be disabled

EXAMPLE:
entityManager.find(User.class, 1); // DB hit
entityManager.find(User.class, 1); // From L1 cache

MEMORY TRICK 🧠:
"1st = Session"
*/

/*
------------------------------------------------------------------------------
2️⃣ SECOND LEVEL CACHE (L2 CACHE)
------------------------------------------------------------------------------
• Optional
• Shared across sessions
• Needs provider (Ehcache, Redis, Hazelcast)
• Entity-level cache

USED WHEN:
✔ Read-heavy data
✔ Reference/master data

ANNOTATION:
@Cacheable

MEMORY TRICK 🧠:
"2nd = Shared"
*/

/*
------------------------------------------------------------------------------
3️⃣ QUERY CACHE
------------------------------------------------------------------------------
• Caches query result
• Must enable explicitly
• Depends on L2 cache

⚠️ Rarely used
⚠️ Risky if data changes frequently
*/

/*
------------------------------------------------------------------------------
REDIS vs HIBERNATE L2 CACHE (INTERVIEW FAVORITE)
------------------------------------------------------------------------------
Hibernate L2 Cache:
✔ ORM-managed
✔ Entity-level
❌ Limited control

Redis:
✔ Distributed
✔ Language-agnostic
✔ Manual control
✔ Used for API caching

INTERVIEW LINE ⭐:
"I use Hibernate cache for entity optimization
and Redis for application-level caching."
*/

/*
===============================================================================
SECTION 2: @TRANSACTIONAL PROPAGATION (🔥 CRITICAL)
===============================================================================

WHAT IS TRANSACTION?
• Logical unit of work
• Either fully commit or rollback
*/

/*
------------------------------------------------------------------------------
PROPAGATION TYPES (MOST IMPORTANT)
------------------------------------------------------------------------------

REQUIRED (DEFAULT):
• Join existing tx
• Create new if none exists

REQUIRES_NEW:
• Suspends existing tx
• Always creates new tx

SUPPORTS:
• Join if exists
• Else run without tx

NOT_SUPPORTED:
• Suspends tx
• Runs without tx

MANDATORY:
• Must have existing tx
• Else throws exception

NEVER:
• Must NOT have tx
• Else throws exception
*/

/*
------------------------------------------------------------------------------
REAL BUG #1: REQUIRES_NEW MISUSE
------------------------------------------------------------------------------
BUG:
Outer tx fails → inner tx still commits ❌

WHY?
REQUIRES_NEW commits independently.

INTERVIEW LINE:
"REQUIRES_NEW can cause partial commits if misused."
*/

/*
------------------------------------------------------------------------------
REAL BUG #2: @Transactional NOT WORKING
------------------------------------------------------------------------------
CAUSE:
• Self-invocation
• Method called inside same class

WHY?
Spring uses proxy → proxy not triggered

FIX:
✔ Move method to another service
✔ Call via injected bean

INTERVIEW LINE ⭐:
"Transactional doesn’t work with self-invocation."
*/

/*
===============================================================================
SECTION 3: LazyInitializationException (🔥 VERY COMMON)
===============================================================================

WHAT IS IT?
Exception when accessing LAZY entity
OUTSIDE Hibernate session.

ERROR:
could not initialize proxy – no Session
*/

/*
------------------------------------------------------------------------------
WHY IT HAPPENS?
------------------------------------------------------------------------------
• Session closed
• Entity accessed outside @Transactional
• LAZY collection accessed in controller
*/

/*
------------------------------------------------------------------------------
BAD CODE (CLASSIC BUG)
------------------------------------------------------------------------------
Order order = orderRepo.findById(1);
order.getItems().size(); // 💥 LazyInitializationException
*/

/*
------------------------------------------------------------------------------
FIXES (IN ORDER OF PREFERENCE)
------------------------------------------------------------------------------

FIX #1: FETCH JOIN (BEST)
✔ Explicit
✔ Fast
✔ Safe

FIX #2: @Transactional (Service layer)
✔ Session open
❌ Risk of loading too much data

FIX #3: DTO Projection
✔ Best for APIs
✔ No entity exposure

FIX #4: Open Session in View (❌ AVOID)
✔ Masks problem
❌ Bad for performance

INTERVIEW LINE ⭐:
"I prefer fetch joins or DTOs instead of OSIV."
*/

/*
===============================================================================
SECTION 4: SPRING BOOT JPA PRODUCTION CHECKLIST (🔥 MUST MEMORIZE)
===============================================================================

✔ Use LAZY loading by default
✔ Fix N+1 queries
✔ Use fetch join / EntityGraph
✔ Add DB indexes
✔ Enable pagination
✔ Avoid findAll() on large tables
✔ Use @Transactional(readOnly = true) for reads
✔ Keep transactions short
✔ Use DTOs for APIs
✔ Cache wisely (Hibernate vs Redis)
✔ Enable SQL logs only in non-prod

MEMORY TRICK 🧠:
"L N F I P T D C"

L → Lazy
N → N+1
F → Fetch join
I → Index
P → Pagination
T → Transaction
D → DTO
C → Cache
*/

/*
===============================================================================
FINAL INTERVIEW SCRIPT (🔥 PERFECT ANSWER)
===============================================================================

"In Spring Boot JPA, I focus on lazy loading,
fix N+1 queries using fetch joins or EntityGraph,
use batch fetching for pagination,
apply caching carefully using Hibernate or Redis,
keep transactions short with proper propagation,
avoid LazyInitializationException using fetch joins or DTOs,
and follow production best practices like indexing and pagination."

================================================================================
END OF FILE
================================================================================
*/

public class SpringBootJpaMasterInterviewNotes {
    // Read once.
    // Revise before interview.
    // Answer like a senior engineer.
}
