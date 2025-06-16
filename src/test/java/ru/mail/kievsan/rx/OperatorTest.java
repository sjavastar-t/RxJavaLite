package ru.mail.kievsan.rx;

import ru.mail.kievsan.rx.core.Observable;
import ru.mail.kievsan.rx.operators.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class OperatorTest {

    @Test
    void testMapAndFilter() {
        Observable<Integer> src = Observable.create(o -> {
            o.onNext(1);
            o.onNext(2);
            o.onNext(3);
            o.onComplete();
        });

        List<Integer> result = new ArrayList<>();
        Observable<Integer> mapped = MapOperator.apply(src, i -> i * 10);
        Observable<Integer> filtered = FilterOperator.apply(mapped, i -> i > 10);
        filtered.subscribe(result::add);

        assertEquals(List.of(20, 30), result);
    }

    @Test
    void testReduce() {
        Observable<Integer> src = Observable.create(o -> {
            o.onNext(1);
            o.onNext(2);
            o.onNext(3);
            o.onComplete();
        });

        List<Integer> out = new ArrayList<>();
        Observable<Integer> reduced = ReduceOperator.apply(src, Integer::sum);
        reduced.subscribe(out::add, Throwable::printStackTrace, () -> {});

        assertEquals(List.of(6), out);
    }

    @Test
    void testMerge() {
        Observable<String> a = Observable.create(o -> {
            o.onNext("a1");
            o.onNext("a2");
            o.onComplete();
        });
        Observable<String> b = Observable.create(o -> {
            o.onNext("b1");
            o.onNext("b2");
            o.onComplete();
        });

        List<String> merged = new ArrayList<>();
        Observable<String> mergedObs = MergeOperator.apply(a, b);
        mergedObs.subscribe(merged::add, Throwable::printStackTrace, () -> {});

        // Все элементы должны быть присутствовать
        assertTrue(merged.containsAll(List.of("a1", "a2", "b1", "b2")));
        assertEquals(4, merged.size());
    }

    @Test
    void testConcat() {
        Observable<String> a = Observable.create(o -> {
            o.onNext("a");
            o.onComplete();
        });
        Observable<String> b = Observable.create(o -> {
            o.onNext("b");
            o.onComplete();
        });

        List<String> out = new ArrayList<>();
        Observable<String> concatObs = ConcatOperator.apply(a, b);
        concatObs.subscribe(out::add);

        assertEquals(List.of("a", "b"), out);
    }

    @Test
    void testFlatMap() {
        Observable<Integer> src = Observable.<Integer>create(o -> {
            o.onNext(1);
            o.onNext(2);
            o.onComplete();
        });

        List<Integer> out = new ArrayList<>();
        Observable<Integer> flat = FlatMapOperator.apply(src, i ->
                Observable.just(i, i * 100)
        );
        flat.subscribe(out::add);

        assertEquals(4, out.size());
        assertTrue(out.containsAll(List.of(1, 100, 2, 200)));
    }
}
