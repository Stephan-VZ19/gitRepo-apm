package ch.fhnw.apm.decrytor;

import java.util.stream.DoubleStream;

public class DecryptorPerfTester {

    private static final int REPETITIONS = 30;

    public static void main(String[] args) {
        var encrypted = Encryptor.encrypt("""
                Very secret text that must under no circumstance be revealed to the
                public or else terrible things will happen. Seriously.
                """, "dywq");

        var latencies = new double[REPETITIONS];
        for (int i = 0; i < REPETITIONS; i++) {
            var startTime = System.nanoTime();

            Decryptor.decrypt(encrypted);

            var latency = System.nanoTime() - startTime;
            latencies[i] = latency / 1_000_000.0; // convert to ms

            // print progress to err
            if ((i + 1) % 10 == 0) {
                System.err.println(i + 1 + "/" + REPETITIONS + " repetitions");
            }
        }
        System.err.println();

        for (int i = 0; i < REPETITIONS; i++) {
            System.out.printf("%.1f\n", latencies[i]);
        }
        System.out.println();

        var stats = DoubleStream.of(latencies).summaryStatistics();
        System.out.printf("Average: %.1f ms\n", stats.getAverage());
        System.out.printf("Min: %.1f ms\n", stats.getMin());
        System.out.printf("Max: %.1f ms\n", stats.getMax());
    }
}
