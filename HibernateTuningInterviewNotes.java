/*
================================================================================
                HIBERNATE PERFORMANCE TUNING
                    SDE-2 INTERVIEW NOTES
================================================================================

GOAL OF THIS FILE:
✔ Hibernate performance tuning concepts
✔ Common production problems
✔ Solutions + best practices
✔ MEMORY TRICKS to remember everything
✔ ONE FILE – ZERO CONFUSION

READ THIS BEFORE INTERVIEW.
================================================================================
*/

/*
------------------------------------------------------------------------------
SECTION 1: GOLDEN RULE (INTERVIEW OPENER)
------------------------------------------------------------------------------
"Hibernate performance problems usually come from:
 Lazy loading,
 N+1 queries,
 Wrong fetch strategy,
 Missing indexes,
 Poor caching strategy."
*/

/*
------------------------------------------------------------------------------
SECTION 2: FETCH TYPE (LAZY vs EAGER)
------------------------------------------------------------------------------
DEFAULT:
• @ManyToOne → EAGER
• @OneToMany → LAZY

LAZY:
• Data loaded ONLY when accessed
• Better performance
• Can cause LazyInitializationException

EAGER:
• Loads immediately
• Can cause performance issues
• Dangerous in production

BEST PRACTICE:
❌ Avoid EAGER in production
✔ Use LAZY + fetch explicitly when needed

MEMORY TRICK 🧠:
"L = Load Later (good)
 E = Load Everything (danger)"
*/

/*
------------------------------------------------------------------------------
SECTION 3: N+1 QUERY PROBLEM (VERY COMMON)
------------------------------------------------------------------------------
WHAT IS N+1?
• 1 query for parent
• N queries for children
• Total = N + 1 queries

EXAMPLE:
SELECT * FROM orders;          -- 1 query
SELECT * FROM items WHERE id=? -- N queries

WHY IT IS BAD:
• DB overload
• Slow APIs
• Seen only in production

FIXES:
✔ fetch join
✔ @EntityGraph
✔ Batch fetching
✔ Proper JPQL

MEMORY TRICK 🧠:
"N+1 = N queries TOO MANY"
*/

/*
------------------------------------------------------------------------------
SECTION 4: FETCH JOIN (BEST FIX)
------------------------------------------------------------------------------
JPQL FETCH JOIN:

SELECT o FROM Order o
JOIN FETCH o.items

WHAT IT DOES:
• Loads parent + child in SINGLE query
• Prevents N+1

INTERVIEW LINE:
"Fetch join is the safest and most explicit way to solve N+1."
*/

/*
------------------------------------------------------------------------------
SECTION 5: BATCH FETCHING
------------------------------------------------------------------------------
WHAT IS IT?
• Hibernate loads entities in batches instead of one-by-one

CONFIG:
hibernate.default_batch_fetch_size=10

OR:
@BatchSize(size = 10)

WHEN TO USE:
• Large collections
• Pagination scenarios

MEMORY TRICK 🧠:
"Batch = Bring in BULK"
*/

/*
------------------------------------------------------------------------------
SECTION 6: HIBERNATE CACHING (VERY IMPORTANT)
------------------------------------------------------------------------------

LEVEL 1 CACHE (FIRST LEVEL):
• Enabled by default
• Session scoped
• One DB hit per entity per session
• Cannot be disabled

LEVEL 2 CACHE (SECOND LEVEL):
• Optional
• Shared across sessions
• Needs provider (Ehcache, Redis, etc.)

QUERY CACHE:
• Caches query result
• Must enable explicitly

MEMORY TRICK 🧠:
"1st = Session
 2nd = Shared"
*/

/*
------------------------------------------------------------------------------
SECTION 7: DIRTY CHECKING (SILENT PERFORMANCE HIT)
------------------------------------------------------------------------------
WHAT IS DIRTY CHECKING?
• Hibernate tracks entity changes automatically
• On commit, compares old vs new state

PROBLEM:
• Too many managed entities = slow commit

SOLUTION:
✔ Use readOnly transactions
✔ Detach entities when not needed
✔ Avoid long sessions

INTERVIEW LINE:
"Dirty checking is powerful but expensive if misused."
*/

/*
------------------------------------------------------------------------------
SECTION 8: TRANSACTION TUNING
------------------------------------------------------------------------------
BEST PRACTICES:
✔ Keep transactions short
✔ Avoid business logic inside transaction
✔ Use @Transactional(readOnly = true) for reads

WHY?
• Reduces dirty checking
• Improves performance

MEMORY TRICK 🧠:
"Transaction = Short & Sweet"
*/

/*
------------------------------------------------------------------------------
SECTION 9: INDEXING (MOST IGNORED BUT POWERFUL)
------------------------------------------------------------------------------
WHAT TO INDEX:
✔ Foreign keys
✔ Columns used in WHERE
✔ JOIN columns

IMPORTANT:
• Hibernate does NOT auto-create indexes
• DB tuning is your responsibility

INTERVIEW LINE:
"No Hibernate tuning works without proper DB indexing."
*/

/*
------------------------------------------------------------------------------
SECTION 10: PAGINATION (VERY IMPORTANT)
------------------------------------------------------------------------------
USE:
• Pageable
• setFirstResult()
• setMaxResults()

WHY?
• Prevents loading huge data into memory
• Improves API response time

BAD PRACTICE:
❌ findAll() on large tables

MEMORY TRICK 🧠:
"Big data? Always paginate."
*/

/*
------------------------------------------------------------------------------
SECTION 11: COMMON ANTI-PATTERNS (RED FLAGS)
------------------------------------------------------------------------------
❌ FetchType.EAGER everywhere
❌ findAll() on large tables
❌ No indexes
❌ Long-running transactions
❌ Ignoring N+1

INTERVIEW TIP:
Mention these to sound senior.
*/

/*
------------------------------------------------------------------------------
SECTION 12: FINAL MEMORY FORMULA (🔥 MUST MEMORIZE)
------------------------------------------------------------------------------
L N C B T I P

L → Lazy loading
N → N+1 problem
C → Cache (1st & 2nd level)
B → Batch fetching
T → Transaction tuning
I → Indexing
P → Pagination

SAY THIS IN INTERVIEW:
"I usually tune Hibernate by checking Lazy loading,
N+1 queries, caching, batch fetching,
transaction boundaries, indexing, and pagination."
*/

/*
------------------------------------------------------------------------------
SECTION 13: FINAL INTERVIEW SCRIPT (PERFECT ANSWER)
------------------------------------------------------------------------------
"Hibernate performance issues usually come from wrong fetch strategies
and N+1 queries. I prefer LAZY loading with fetch joins,
use batch fetching for large collections,
enable caching where needed,
keep transactions short,
add proper DB indexes,
and always paginate large queries."
================================================================================
END OF FILE
================================================================================
*/

public class HibernateTuningInterviewNotes {
    // Read it.
    // Remember the tricks.
    // Crack the interview.
}
