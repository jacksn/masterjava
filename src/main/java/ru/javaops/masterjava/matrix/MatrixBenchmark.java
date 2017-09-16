package ru.javaops.masterjava.matrix;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * gkislin
 * 23.09.2016
 */
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@BenchmarkMode({Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@Timeout(time = 5, timeUnit = TimeUnit.MINUTES)
public class MatrixBenchmark {
    // Matrix size
    @Param({"100", "1000"})
    private int matrixSize;

    private static final int THREAD_NUMBER = 10;
    private final static ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);

    private static int[][] matrixA;
    private static int[][] matrixB;

    @Setup
    public void setUp() {
        matrixA = MatrixUtil.create(matrixSize);
        matrixB = MatrixUtil.create(matrixSize);
    }

    @Benchmark
    public int[][] singleThreadMultiplyOpt() throws Exception {
        return MatrixUtil.singleThreadMultiplyOpt(matrixA, matrixB);
    }

    @Benchmark
    public int[][] concurrentMultiplyStreams() throws Exception {
        return MatrixUtil.concurrentMultiplyStreams(matrixA, matrixB, executor);
    }

    @Benchmark
    public int[][] concurrentMultiply2() throws Exception {
        return MatrixUtil.concurrentMultiply2(matrixA, matrixB, executor);
    }

    @Benchmark
    public int[][] concurrentMultiply3() throws Exception {
        return MatrixUtil.concurrentMultiply3(matrixA, matrixB, executor);
    }

    @TearDown
    public void tearDown() {
        executor.shutdown();
    }
}
