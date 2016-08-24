package ru.sbt.stream;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by Alexander Ushakov on 23.08.2016.
 */
public class Streams<T> {
    private List<T> list;

    private Streams(List<T> list) {
        this.list = list;
    }

    public static <T> Streams<T> of(Collection<T> collection) {
        return new Streams(new ArrayList<T>(collection));
    }

    public Streams<T> filter(Predicate<? super T> predicate) {
        Iterator<T> i = list.iterator();
        while (i.hasNext()) {
            T next = i.next();
            if (!predicate.test(next)) {
                i.remove();
            }
        }
        return this;
    }

    public <R> Streams<R> transform(Function<? super T, ? extends R> f) {
        List<R> list = new ArrayList<R>();
        for (T t : this.list) {
            list.add(f.apply(t));
        }
        return new Streams<R>(list);
    }

    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper,
                                  Function<? super T, ? extends V> valueMapper) {
        Map<K, V> map = new HashMap<K, V>();
        for (T t : list) {
            map.put(keyMapper.apply(t), valueMapper.apply(t));
        }
        return map;
    }

    public static void main(String[] args) {
        List<Person> someCollection = new ArrayList<>();
        someCollection.add(new Person("Alex", 24));
        someCollection.add(new Person("Bob", 86));
        someCollection.add(new Person("Chris", 15));
        Map<String, Person> m = Streams.of(someCollection)
                .filter(p -> p.getAge() > 20)
                .transform(p -> new Person(p.getName(), p.getAge() + 30))
                .toMap(p -> p.getName(), p -> p);
        m.entrySet().forEach(e -> System.out.println(e.getKey() + " " + e.getValue().getAge()));
    }
}
