package ru.otus.courses.java.advanced.shooter.server.common.jmh.cache.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.map.wrapper.LockMapWrapper;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.map.wrapper.ReadWriteLockMapWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Threads(Threads.MAX)
@BenchmarkMode(Mode.All)
@Fork(value = 3)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 3, time = 30)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class HashingBenchmarks {

    private static final String DATA_1 = "TEST_DATA_1";
    private static final String DATA_2 = "TEST_DATA_2";

    @State(Scope.Benchmark)
    public static class ExecutionState {
        @Param({"1000000"})
        public int keysCount;

        public Map<Integer, String> concurrentHashMap;
        public Map<Integer, String> lockMap;
        public Map<Integer, String> readWriteLockMap;

        @Setup(Level.Trial)
        public void setup() {
            concurrentHashMap = new ConcurrentHashMap<>(keysCount * 2);
            for (int i = 0; i < keysCount; i++) {
                concurrentHashMap.put(i, DATA_1);
            }

            lockMap = new LockMapWrapper<>(new HashMap<>(keysCount * 2));
            for (int i = 0; i < keysCount; i++) {
                lockMap.put(i, DATA_1);
            }

            readWriteLockMap = new ReadWriteLockMapWrapper<>(new HashMap<>(keysCount * 2));
            for (int i = 0; i < keysCount; i++) {
                readWriteLockMap.put(i, DATA_1);
            }
        }
    }

    @Benchmark
    @Group("concurrentHashMap")
    @GroupThreads(5)
    public void concurrentHashMapRead(Blackhole blackhole, ExecutionState executionState) {
        int key = ThreadLocalRandom.current().nextInt(executionState.keysCount);
        blackhole.consume(executionState.concurrentHashMap.get(key));
    }

    @Benchmark
    @Group("concurrentHashMap")
    @GroupThreads(5)
    public void concurrentHashMapWrite(Blackhole blackhole, ExecutionState executionState) {
        int key = ThreadLocalRandom.current().nextInt(executionState.keysCount);
        blackhole.consume(executionState.concurrentHashMap.put(key, DATA_2));
    }

    @Benchmark
    @Group("lockMap")
    @GroupThreads(5)
    public void lockMapRead(Blackhole blackhole, ExecutionState executionState) {
        int key = ThreadLocalRandom.current().nextInt(executionState.keysCount);
        blackhole.consume(executionState.lockMap.get(key));
    }

    @Benchmark
    @Group("lockMap")
    @GroupThreads(5)
    public void lockMapWrite(Blackhole blackhole, ExecutionState executionState) {
        int key = ThreadLocalRandom.current().nextInt(executionState.keysCount);
        blackhole.consume(executionState.lockMap.put(key, DATA_2));
    }


    @Benchmark
    @Group("readWriteLockMap")
    @GroupThreads(5)
    public void readWriteLockMapRead(Blackhole blackhole, ExecutionState executionState) {
        int key = ThreadLocalRandom.current().nextInt(executionState.keysCount);
        blackhole.consume(executionState.readWriteLockMap.get(key));
    }

    @Benchmark
    @Group("readWriteLockMap")
    @GroupThreads(5)
    public void readWriteLockMapWrite(Blackhole blackhole, ExecutionState executionState) {
        int key = ThreadLocalRandom.current().nextInt(executionState.keysCount);
        blackhole.consume(executionState.readWriteLockMap.put(key, DATA_2));
    }
}
