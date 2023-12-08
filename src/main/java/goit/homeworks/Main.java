package IK.stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Main {

    public static void main(String[] args) {

        // DATA
        List<String> names = new ArrayList<>();
        names.add("Mike");
        names.add("John");
        names.add("Elsa");
        names.add("Gina");
        names.add("Denis");

        String[] numbers = new String[] {"1, 2, 0", "4, 5"};

        long a = 25214903917L;
        long c = 11L;
        long m = (long) Math.pow(2, 48);

        Stream<String> firstStream = Stream.of("J", "A", "S", "P", "E", "R");
        Stream<String> secondStream = Stream.of("1", "2", "6", "8");

        // TASK 1;
        var oddNames = getIndexedOddElementsFromList(names);
        System.out.println(String.join(", ", oddNames));

        // TASK 2;
        var sortedUppercase = makeUppercaseAndSortDescending(names);
        System.out.println(String.join(", ", sortedUppercase));

        // TASK 3;
        System.out.println(sortNumbersArrayToString(numbers));

        // TASK 4;
        RandomNumberGenerator rng = new RandomNumberGenerator();
        rng.generateLimited(a, c, m, 10); // use generate() method to get unlimited result

        // TASK 5;
        Stream<String> zippedStream = zip(firstStream, secondStream);
        zippedStream.forEach(System.out::print);

    }

    public static String sortNumbersArrayToString(String[] array) {
        return Arrays.stream(array)
                .flatMap(s -> Arrays.stream(s.split(", ")))
                .mapToInt(Integer::parseInt)
                .sorted()
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    public static List<String> getIndexedOddElementsFromList(List<String> list) {
        return IntStream.range(0, list.size())
                .filter(i -> i % 2 != 0)
                .mapToObj(i -> (i) + ". " + list.get(i))
                .collect(Collectors.toList());
    }

    public static List<String> makeUppercaseAndSortDescending(List<String> list) {
        return list.stream()
                .map(String::toUpperCase)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
        Iterator<T> iteratorFirst = first.iterator();
        Iterator<T> iteratorSecond = second.iterator();

        Iterable<T> iterable = () -> new Iterator<>() {
            private boolean switcher = true;

            @Override
            public boolean hasNext() {
                if (switcher) {
                    return iteratorFirst.hasNext() && iteratorSecond.hasNext();
                } else {
                    return iteratorSecond.hasNext() && iteratorFirst.hasNext();
                }
            }

            @Override
            public T next() {
                switcher = !switcher;
                return switcher ? iteratorSecond.next() : iteratorFirst.next();
            }
        };

        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static class RandomNumberGenerator {

        private long seed;

        public RandomNumberGenerator() {
            seed = System.currentTimeMillis();
        }

        public RandomNumberGenerator(long seed) {
            this.seed = seed;
        }

        public void setSeed(long seed) {
            this.seed = seed;
        }

        private Stream<Long> linearCongruentialGenerator(long a, long c, long m, long seed) {
            return Stream.iterate(seed, x -> (a * x + c) % m);
        }

        public void generateLimited(long a, long c, long m, long limit) {
            linearCongruentialGenerator(a, c, m, seed)
                    .limit(limit)
                    .forEach(System.out::println);
        }

        public void generate(long a, long c, long m) {
            linearCongruentialGenerator(a, c, m, seed)
                    .forEach(System.out::println);
        }
    }

}
