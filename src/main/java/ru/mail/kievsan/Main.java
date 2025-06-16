package ru.mail.kievsan;

import ru.mail.kievsan.rx.core.Observable;
import ru.mail.kievsan.rx.operators.*;
import ru.mail.kievsan.rx.schedulers.ComputationScheduler;
import ru.mail.kievsan.rx.schedulers.Scheduler;


public class Main {
    public static void main(String[] args) {
        try (Scheduler scheduler = new ComputationScheduler()) {
            System.out.println("\n\t map, filter:");
            Observable<Integer> source = Observable.range(1, 10);
            FilterOperator.apply(MapOperator.apply(source, i -> i * 10), i -> i > 20)
//                    .observeOn(scheduler)
                    .subscribeOn(scheduler)
                    .subscribe(i -> System.out.println("Received... " + i),
                            Throwable::printStackTrace,
                            () -> System.out.println("Completed"))
            ;
            Thread.sleep(1000);

            System.out.println("\n\t flatMap:");
            FlatMapOperator.apply(source, i -> Observable.just(i, i * 10))
                    .subscribe(i -> System.out.println("Received... " + i));
            Thread.sleep(1000);

            System.out.println("\n\t merge:");
            MergeOperator.apply(
                    Observable.just("a1", "a2"),
                    Observable.just("b1", "b2")
            ).subscribe(s -> System.out.println("Received... " + s));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
