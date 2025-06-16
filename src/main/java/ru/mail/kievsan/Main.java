package ru.mail.kievsan;


import ru.mail.kievsan.rx.core.Disposable;
import ru.mail.kievsan.rx.core.Observable;
import ru.mail.kievsan.rx.operators.FilterOperator;
import ru.mail.kievsan.rx.operators.MapOperator;
import ru.mail.kievsan.rx.schedulers.ComputationScheduler;
import ru.mail.kievsan.rx.schedulers.IOScheduler;
import ru.mail.kievsan.rx.schedulers.Scheduler;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        System.out.println("\n\tmap, filter:\t");
        try (Scheduler scheduler = new ComputationScheduler()) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
