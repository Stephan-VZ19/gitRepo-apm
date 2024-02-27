package ch.fhnw.apm.moviedb;

import org.openjdk.jmh.annotations.Benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.List;

@State(Scope.Benchmark)
public class MovieDBBenchmark {

    MovieDB movieDB;

    @Setup
    public void setup() {
        movieDB = new MovieDB(Path.of("../data"));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Fork(1)
    @Threads(8)
    @Warmup(iterations = 5, time = 2)
    @Measurement(iterations = 2, time = 2)
    public List<Movie> mostPopularMoviesPerYear() {
        return movieDB.mostPopularMoviesByYear(2023, 50);
    }

    public static void main(String[] args) throws RunnerException, IOException {
        org.openjdk.jmh.Main.main(new String[] {MovieDBBenchmark.class.getName()});
    }

}
