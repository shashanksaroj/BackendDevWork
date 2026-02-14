/**
 * ============================================================
 *  CONCURRENT MAP — DUMBED DOWN, ADHD-FRIENDLY, SINGLE FILE
 * ============================================================
 *
 * PROBLEM:
 * Multiple threads want to read & write a Map at the same time.
 *
 * GOAL:
 * - No data corruption
 * - High performance
 *
 * WHY NOT HashMap?
 * - Not thread-safe
 * - Race conditions
 *
 * WHY NOT Hashtable / synchronizedMap?
 * - Thread-safe BUT one big lock
 * - Very slow under concurrency
 *
 * SOLUTION:
 * - ConcurrentMap / ConcurrentHashMap
 *
 * This file shows:
 * 1. WRONG naive approach (global lock)
 * 2. BETTER approach (lock striping)
 * 3. WHY atomic operations matter
 *
 * NOTE:
 * This is EDUCATIONAL.
 * Real ConcurrentHashMap is MUCH more complex.
 */

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentMapExplained {

    /* =========================================================
       1️⃣ NAIVE IMPLEMENTATION (THREAD-SAFE BUT SLOW ❌)
       =========================================================
       - One big synchronized lock
       - Same as Hashtable
       - Only ONE thread can access at a time
    */
    static class NaiveConcurrentMap<K, V> {
        private final Map<K, V> map = new HashMap<>();

        public synchronized V get(K key) {
            return map.get(key);
        }

        public synchronized void put(K key, V value) {
            map.put(key, value);
        }
    }

    /* =========================================================
       2️⃣ BETTER IMPLEMENTATION — LOCK STRIPING ✅
       =========================================================
       CORE IDEA:
       👉 Do NOT lock the whole map
       👉 Lock only the bucket involved

       ANALOGY:
       - City = Map
       - Areas = Buckets
       - Police lock ONE area, not whole city
    */
    static class MyConcurrentMap<K, V> {

        // Number of buckets (like segments)
        private static final int BUCKET_COUNT = 16;

        // Each bucket is a normal HashMap
        private final Map<K, V>[] buckets;

        // Each bucket has its OWN lock
        private final Object[] locks;

        @SuppressWarnings("unchecked")
        public MyConcurrentMap() {
            buckets = new HashMap[BUCKET_COUNT];
            locks = new Object[BUCKET_COUNT];

            for (int i = 0; i < BUCKET_COUNT; i++) {
                buckets[i] = new HashMap<>();
                locks[i] = new Object();
            }
        }

        // Decide which bucket the key belongs to
        private int getBucketIndex(K key) {
            return Math.abs(key.hashCode() % BUCKET_COUNT);
        }

        // Thread-safe PUT
        public void put(K key, V value) {
            int index = getBucketIndex(key);

            synchronized (locks[index]) {
                buckets[index].put(key, value);
            }
        }

        // Thread-safe GET
        public V get(K key) {
            int index = getBucketIndex(key);

            synchronized (locks[index]) {
                return buckets[index].get(key);
            }
        }
    }

    /* =========================================================
       3️⃣ WHY ATOMIC OPERATIONS MATTER (VERY IMPORTANT 🔥)
       =========================================================
       This is where MOST concurrency bugs happen.
    */

    public static void main(String[] args) throws Exception {

        /* ---------------------------------------------
           ❌ WRONG WAY (Race condition)
           ---------------------------------------------
           Read → Modify → Write are separate steps
        */
        Map<String, Integer> unsafeMap = new HashMap<>();
        unsafeMap.put("count", 0);

        ExecutorService executor1 = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            executor1.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    Integer val = unsafeMap.get("count");
                    unsafeMap.put("count", val + 1); // ❌ NOT ATOMIC
                }
            });
        }
        executor1.shutdown();
        executor1.awaitTermination(1, TimeUnit.SECONDS);

        System.out.println("❌ HashMap result (wrong): " + unsafeMap.get("count"));

        /* ---------------------------------------------
           ✅ CORRECT WAY (Atomic operation)
           ---------------------------------------------
           compute() = read + update + write AS ONE STEP
        */
        Map<String, Integer> safeMap = new ConcurrentHashMap<>();
        safeMap.put("count", 0);

        ExecutorService executor2 = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            executor2.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    safeMap.compute("count", (k, v) -> v + 1);
                }
            });
        }
        executor2.shutdown();
        executor2.awaitTermination(1, TimeUnit.SECONDS);

        System.out.println("✅ ConcurrentHashMap result: " + safeMap.get("count"));

        /* ---------------------------------------------
           BONUS: AtomicInteger (when key not needed)
        */
        AtomicInteger atomicCounter = new AtomicInteger(0);
        ExecutorService executor3 = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 4; i++) {
            executor3.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    atomicCounter.incrementAndGet();
                }
            });
        }
        executor3.shutdown();
        executor3.awaitTermination(1, TimeUnit.SECONDS);

        System.out.println("✅ AtomicInteger result: " + atomicCounter.get());
    }
}

/*
============================================================
🧠 FINAL TL;DR (INTERVIEW GOLD)
============================================================

HashMap
- ❌ Not thread-safe
- ✅ Fast (single thread)

Hashtable / synchronizedMap
- ✅ Thread-safe
- ❌ One big lock → slow

ConcurrentHashMap
- ✅ Thread-safe
- ✅ High performance
- ✅ Lock striping + CAS
- ✅ Atomic methods (compute, putIfAbsent)

Interview Answer:
"I would partition the map into buckets using hashing,
lock only the affected bucket, and use atomic operations
for updates to avoid race conditions."
============================================================
*/
