package ch.fhnw.apm.hello;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;

import java.io.IOException;

public class GreeterBenchmark {
    // TODO

    @Benchmark
    @Fork(1)
    public void generateGreeting() {

        var greeter = new Greeter(100);
        greeter.generateGreeting();
    }

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }
}
