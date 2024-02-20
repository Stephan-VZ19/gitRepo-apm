package ch.fhnw.apm.factorize;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Factorization {

    private static final List<Long> NUMBERS = List.of(
            1801086419398447L, 2171458689911351L, 2301023258870333L, 3522887383695761L,
            4132443495634673L, 4081208032457939L, 3485403855550147L, 2508416454261463L,
            2325658989972167L, 4839822750477853L, 2618862330280879L, 3267479089417663L,
            4068476091035773L, 1367704700701349L, 5227472439928243L, 2625903007545709L,
            4145986577851483L, 3207855129948713L, 4166985675735821L, 3270883505411339L,
            2386540523637007L, 4505940476524769L, 3317223687834709L, 2927304224249227L,
            3089653114179559L, 2974607993636371L, 4237143627152851L, 4631489762503403L,
            3679692338921251L, 2072614596151159L, 3466067599168877L, 4338929258986919L,
            2722075059037937L, 4104580669918051L, 2696999737469969L, 4089719383465399L,
            4358614072805483L, 2911814682479173L, 2365491335580397L, 3395357263770833L,
            2201739326206933L, 5216506783209587L, 3242238933574117L, 2659114516930439L,
            3220121400497119L, 1666302744851609L, 2378235567772951L, 3233219312658259L,
            1922637665220841L, 2345672458409107L, 2468875717396133L, 2383835413496057L,
            2356506134761283L, 2999812801728079L, 2771028572281829L, 2378788417735859L,
            3439907486132413L, 4260027458841461L, 5926967363704249L, 3582769260663167L,
            2498721767043643L, 3103595452748081L, 3868719454402097L, 4037870412874117L);

    public static void main(String[] args) {
        var times = new ArrayList<Double>();
        for (int i = 0; i < 20; i++) {
            var start = System.nanoTime();

            factorizeAll();

            double time = (System.nanoTime() - start) / 1_000_000.0;
            times.add(time);
        }

        for (int i = 0; i < 20; i++) {
            System.out.printf("%.2f ms\n", times.get(i));
        }
    }

    public static void factorizeAll() {
        try (var threadPool = Executors.newVirtualThreadPerTaskExecutor()) {
            for (var n : NUMBERS) {
                threadPool.execute(() -> {
                    var factors = factorize(n);
                    System.out.println(n + ": " + factors);
                });
            }
        };
    }

    public static List<Long> factorize(long num) {
        var factors = new ArrayList<Long>();
        long factor = 2;
        while (factor <= num) {
            while (num % factor == 0) {
                factors.add(factor);
                num /= factor;
                if (num == 1) {
                    return factors;
                }
            }
            factor++;
        }
        throw new AssertionError();
    }
}
