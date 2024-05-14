package ch.fhnw.apm.amdahl;

import java.util.concurrent.Executors;

public class AmdahlsLaw {

    public static void main(String[] args) {
        var parallelism = 2;

        var start = System.nanoTime();
        prepare();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < parallelism; i++) {
                executor.submit(() -> {
                    doWork(1.0 / parallelism);
                });
            }
        }

        var time = (System.nanoTime() - start) / 1_000_000_000.0;
        System.out.printf("%.2f s", time);
    }

    private static void prepare() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
    }

    private static void doWork(double ratio) {
        try {
            Thread.sleep((long) (9000 * ratio));
        } catch (InterruptedException ignored) {}
    }
}
