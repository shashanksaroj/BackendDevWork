/*
================================================================================
              JDBC vs JPA vs HIBERNATE
                SPRING BOOT INTERVIEW NOTES
================================================================================

GOAL OF THIS FILE:
✔ Prove JDBC, JPA, Hibernate relationship
✔ Show differences clearly
✔ Explain how Spring Boot uses them
✔ Give interview-ready one-liners
✔ Everything in ONE FILE (no confusion)

READ THIS FILE TOP → BOTTOM BEFORE INTERVIEW.
================================================================================
*/

/*
------------------------------------------------------------------------------
SECTION 1: JDBC (JAVA DATABASE CONNECTIVITY)
------------------------------------------------------------------------------
WHAT IS JDBC?
• Low-level Java API to communicate with databases
• You write SQL manually
• You manage:
  - Connection
  - Statement
  - ResultSet
  - Transactions

IMPORTANT:
• JDBC talks DIRECTLY to database
• NO ORM
• VERY VERBOSE
*/

/*
JDBC EXAMPLE (LOW LEVEL)

Connection con = dataSource.getConnection();
PreparedStatement ps =
    con.prepareStatement("SELECT * FROM users WHERE id=?");
ps.setInt(1, 1);
ResultSet rs = ps.executeQuery();

Problems with JDBC:
❌ Too much boilerplate
❌ Hard to maintain
❌ Error-prone
❌ Not scalable for large apps

INTERVIEW ONE-LINER:
"JDBC is low-level and verbose, so we avoid using it directly in Spring Boot."
*/

/*
------------------------------------------------------------------------------
SECTION 2: JPA (JAVA PERSISTENCE API)
------------------------------------------------------------------------------
WHAT IS JPA?
• JPA is a SPECIFICATION (NOT implementation)
• Defines RULES for ORM
• Contains:
  - Interfaces
  - Annotations
• DOES NOT talk to DB directly

KEY POINT:
JPA = WHAT to do
Hibernate = HOW to do
*/

/*
JPA ANNOTATIONS (ONLY RULES)

@Entity       → Maps class to table
@Id           → Primary key
@OneToMany    → Relationship
@ManyToOne
@Column

JPA CORE INTERFACE:
• EntityManager

JPA QUERY LANGUAGE:
• JPQL (object-oriented, NOT table-based)

IMPORTANT:
❌ JPA alone CANNOT execute SQL
✔ Needs implementation (Hibernate)

INTERVIEW ONE-LINER:
"JPA is just a specification; Hibernate is its implementation."
*/

/*
------------------------------------------------------------------------------
SECTION 3: HIBERNATE (ORM FRAMEWORK)
------------------------------------------------------------------------------
WHAT IS HIBERNATE?
• ORM framework
• Implements JPA
• Converts:
  Java Objects ↔ Database Tables

HIBERNATE USES:
✔ JDBC internally
✔ SQL generation
✔ Connection handling
*/

/*
HIBERNATE RESPONSIBILITIES:
• Generate SQL
• Manage entity lifecycle
• Lazy / Eager loading
• Dirty checking
• Caching
• Transaction handling

HIBERNATE EXTRA FEATURES (BEYOND JPA):
• First-level cache (Session cache)
• Second-level cache
• HQL
• Batch fetching

INTERVIEW ONE-LINER:
"Hibernate is a JPA implementation that provides ORM and performance features."
*/

/*
------------------------------------------------------------------------------
SECTION 4: HOW SPRING BOOT USES JDBC + JPA + HIBERNATE
------------------------------------------------------------------------------
SPRING BOOT REAL FLOW:

Controller
   ↓
Service
   ↓
Repository (Spring Data JPA)
   ↓
Hibernate (JPA implementation)
   ↓
JDBC
   ↓
Database

IMPORTANT:
• You never write JDBC code directly
• Hibernate generates SQL
• JDBC executes SQL
*/

/*
SPRING DATA JPA EXAMPLE:

public interface UserRepository
        extends JpaRepository<User, Long> {
}

WHAT HAPPENS INTERNALLY?
• Spring Data JPA calls Hibernate
• Hibernate converts entity → SQL
• JDBC sends SQL to DB

INTERVIEW GOLD LINE ⭐:
"In Spring Boot, I use Spring Data JPA, which internally uses Hibernate
as the JPA provider and JDBC underneath to communicate with the database."
*/

/*
------------------------------------------------------------------------------
SECTION 5: JDBC vs JPA vs HIBERNATE (COMPARISON)
------------------------------------------------------------------------------

JDBC:
• Type: API
• Level: Low
• SQL: Manual
• ORM: ❌
• Boilerplate: High

JPA:
• Type: Specification
• Level: High
• SQL: JPQL
• ORM: Concept
• Boilerplate: Low

Hibernate:
• Type: Framework
• Level: High
• SQL: Auto-generated
• ORM: ✅
• Boilerplate: Low

INTERVIEW ANSWER:
"JDBC is low-level, JPA defines ORM rules, Hibernate implements those rules."
*/

/*
------------------------------------------------------------------------------
SECTION 6: COMMON INTERVIEW QUESTIONS (READY ANSWERS)
------------------------------------------------------------------------------

Q: Is Hibernate same as JPA?
A: No. JPA is a specification, Hibernate is an implementation.

Q: Can we use Hibernate without JPA?
A: Yes, but not recommended in Spring Boot.

Q: Does Hibernate use JDBC?
A: Yes, internally Hibernate uses JDBC.

Q: Why not JDBC always?
A: Too much boilerplate and no ORM benefits.

Q: What is ORM?
A: Mapping Java objects to database tables automatically.
*/

/*
------------------------------------------------------------------------------
SECTION 7: REAL-WORLD SDE-2 NOTES
------------------------------------------------------------------------------
• For complex business apps → JPA + Hibernate
• For simple high-performance queries → Native SQL / JDBC
• Hibernate performance tuning includes:
  - Fetch type (LAZY vs EAGER)
  - Indexing
  - Batch size
  - Caching
*/

/*
------------------------------------------------------------------------------
SECTION 8: FINAL INTERVIEW SCRIPT (MEMORIZE)
------------------------------------------------------------------------------
"JDBC is a low-level API for database access.
JPA is a specification that defines ORM rules.
Hibernate is the most popular JPA implementation.
In Spring Boot, Spring Data JPA uses Hibernate internally,
and Hibernate uses JDBC underneath to interact with the database."
================================================================================
END OF FILE
================================================================================
*/

public class JdbcJpaHibernateInterviewNotes {
    // Intentionally empty.
    // This file is for INTERVIEW REVISION, not execution.
}
