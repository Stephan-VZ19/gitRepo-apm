package ch.fhnw.apm.hello;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class GreeterBenchmark {
    // TODO

    Greeter greeter;
    @Setup
    public void setup() {
        greeter = new Greeter(100);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Fork(1)
    @Warmup(iterations = 5, time = 2)
    @Measurement(iterations = 2, time = 2)
    public void generateGreeting() {
        greeter.generateGreeting();
    }

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }
}
