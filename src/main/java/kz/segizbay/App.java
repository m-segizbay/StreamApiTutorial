package kz.segizbay;

import com.sun.security.jgss.GSSUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {
    private static class Person {
        enum Position {
            ENGINEER, DIRECTOR, MANAGER
        }
        private String name;
        private int age;
        private Position position;

        public Person(String name, int age, Position position) {
            this.name = name;
            this.age = age;
            this.position = position;
        }

        public String getName() {
            return name;
        }
    }

    private static void printTheMostFrequentWordInFile(){
        try {
            Files.lines(Paths.get("words.txt"))
                    .map(m -> m.split("\\s"))
                    .flatMap(Arrays::stream)
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .sorted((e1, e2) -> e2.getValue().intValue() - e1.getValue().intValue())
                    .limit(1)
                    .findFirst().ifPresent(w -> System.out.println("The mos frequent word in file: " + w));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void countWordsInFile(){
        try {
            Map<String, Long> map = Files.lines(Paths.get("words.txt"))
                    .map(m -> m.split("\\s"))
                    .flatMap(Arrays::stream)
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
            System.out.println(map);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void flatMapEx(){
        try {
            Files.lines(Paths.get("text.txt"))
                    .map(line -> line.split("\\s"))
                    .distinct()
                    .forEach(s -> System.out.println(Arrays.toString(s)));

            System.out.println("-----------------------------");

            Files.lines(Paths.get("text.txt"))
                    .map(line -> line.split("\\s"))
                    .map(Arrays::stream)
                    .distinct()
                    .forEach(s -> System.out.println(s));

            String collect = Files.lines(Paths.get("text.txt"))
                    .map(line -> line.split("\\s"))
                    .flatMap(Arrays::stream)
                    .distinct()
                    .collect(Collectors.joining(", ", "уникальные слова: ", "."));
            System.out.println(collect);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void intStreamEx(){
        IntStream intStream = IntStream.of(1, 2, 3, 4, 5);
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        list.stream().mapToInt(n -> n);
        IntStream.range(1, 10);
        intStream.max();
        intStream.min();
    }

    private static void reduceEx(){
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        int sum = 0;
        for (Integer i : list) {
            sum += i;
        }
        System.out.println(sum );

        int res= list.stream().reduce(0, (a, b) -> a + b);
        System.out.println(res);
    }

    private static int findEx(){
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 15));
        list.stream().filter(n -> n>10).findAny().ifPresent(System.out::println);
        Optional<Integer> i = list.stream().filter(n -> n > 10).findAny();
        return i.orElseThrow(() -> new RuntimeException("Not found"));
    }

    private static void mappingEx(){

        Function<Integer, Integer> cube = n -> (int) Math.pow(n, 3);

        Stream.of(1, 2, 3).map(m -> Math.pow(m, 3));
        Stream.of(1, 2, 3).map(cube);
    }

    private static void countExt(){
        Map<Integer, Long> map = Stream.of("A", "BB", "AA", "B", "C", "NNN")
                .collect(Collectors.groupingBy(String::length, Collectors.counting()));
        System.out.println(map);
    }

    private static void matchEx(){
        List<Integer> list = new ArrayList<>(Arrays.asList(4, 2, 4, 11, 5));
        System.out.println(list.stream().allMatch(n -> n<10));
        System.out.println(list.stream().anyMatch(n -> n==4));
        System.out.println(list.stream().noneMatch(n -> n==2));
    }

    private static void filterEx(){
        Stream.of(1, 2, 3, 5, 6).filter(x -> x % 2 == 0).forEach(new Consumer<Integer>() {

            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        });
    }

    private static void firstExample(){
        List<Person> persons = new ArrayList<>(Arrays.asList(
                new Person("Bob1", 23, Person.Position.ENGINEER),
                new Person("Bob2", 21, Person.Position.MANAGER),
                new Person("Bob3", 45, Person.Position.DIRECTOR),
                new Person("Bob4", 32, Person.Position.ENGINEER),
                new Person("Bob5", 65, Person.Position.MANAGER),
                new Person("Bob6", 23, Person.Position.ENGINEER),
                new Person("Bob7", 53, Person.Position.DIRECTOR),
                new Person("Bob8", 43, Person.Position.MANAGER)
        ));

        List<Person> engineers = new ArrayList<>();
        for (Person person : persons) {
            if (person.position == Person.Position.ENGINEER) {
                engineers.add(person);
            }
        }
        engineers.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.age - o2.age;
            }
        });

        List<String> engineersNames = new ArrayList<>();
        for (Person person : engineers) {
            engineersNames.add(person.getName());
        }
        System.out.println(engineersNames);

        List<String> engineersNamesStreamApi = persons.stream()
                .filter(p -> p.position == Person.Position.ENGINEER)
                .sorted((o1, o2) -> o1.age-o2.age)
                .map(person -> person.getName())
                .limit(2)
                .collect(Collectors.toList());
        System.out.println(engineersNamesStreamApi);
    }

    private static void streamTest(){
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        List<String> newList = list.stream()
                .filter(i -> i % 2 == 0)
                .map(i -> i*i)
                .map(i -> {
                    String res = "";
                    for (int j = 0; j <i ; j++) {
                        res = res + "A";
                    }
                    return res;
                })
                .limit(2)
                .collect(Collectors.toList());
        System.out.println(newList);
    }

    public static void useMyFilter(){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println(myOwnFilter(list, i -> i % 2 == 0));
        System.out.println(myOwnFilter(list, i -> i % 2 != 0));
    }

    public static <T> List<T> myOwnFilter(List<T> list, Predicate<T> predicate){
        List<T> copy = new ArrayList<>(list);
        Iterator<T> iterator = copy.iterator();
        while (iterator.hasNext()) {
            T o = iterator.next();
            if (!predicate.test(o)) {
                iterator.remove();
            }
        }
        return copy;
    }

    public static void main( String[] args ) {
        useMyFilter();
    }
}
