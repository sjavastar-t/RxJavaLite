package ru.mail.kievsan;

import ru.mail.kievsan.rx.core.Observable;
import ru.mail.kievsan.rx.operators.FilterOperator;
import ru.mail.kievsan.rx.operators.FlatMapOperator;
import ru.mail.kievsan.rx.operators.MapOperator;
import ru.mail.kievsan.rx.schedulers.ComputationScheduler;
import ru.mail.kievsan.rx.schedulers.Scheduler;


public class Main {
    public static void main(String[] args) {
        try (Scheduler scheduler = new ComputationScheduler()) {
            System.out.println("\n\t map, filter:");
            Observable<Integer> source = Observable.range(1, 10);
            FilterOperator.apply(MapOperator.apply(source, i -> i * 10), i -> i > 20)
//                    .subscribeOn(scheduler
                    .observeOn(scheduler)
                    .subscribe(
                            i -> System.out.println("Received... " + i),
                            Throwable::printStackTrace,
                            () -> System.out.println("Completed")
                    );
            Thread.sleep(1000);

            System.out.println("\n\t flatMap:");
            FlatMapOperator.apply(source, i -> Observable.just(i, i * 10))
                    .subscribe(i -> System.out.println("flatMap: " + i));
            Thread.sleep(1000);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
