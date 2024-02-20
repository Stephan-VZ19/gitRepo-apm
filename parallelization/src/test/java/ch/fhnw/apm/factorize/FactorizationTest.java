package ch.fhnw.apm.factorize;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactorizationTest {

    @Test
    void factorize() {
        for (long f1 : List.of(2, 3, 5, 7, 11)) {
            for (long f2 : List.of(2, 3, 5, 7, 11)) {
                for (long f3 : List.of(2, 3, 5, 7, 11)) {
                    var num = f1 * f2 * f3;
                    var factors = Factorization.factorize(num);
                    assertEquals(Stream.of(f1, f2, f3).sorted().toList(), factors);
                }
            }
        }
    }

    @Test
    void factorizeLargeFactors() {
        var num = 41L * 1567637L;
        var factors = Factorization.factorize(num);
        assertEquals(List.of(41L, 1567637L), factors);
    }
}
