package ru.mail.kievsan;


import ru.mail.kievsan.rx.core.Observable;
import ru.mail.kievsan.rx.operators.FilterOperator;
import ru.mail.kievsan.rx.operators.MapOperator;
import ru.mail.kievsan.rx.schedulers.ComputationScheduler;
import ru.mail.kievsan.rx.schedulers.IOScheduler;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n\tmap, filter:\t");
        Observable<Integer> source = Observable.range(1, 10);
        FilterOperator.apply(MapOperator.apply(source, i -> i * 10), i -> i > 20)
                .subscribeOn(new IOScheduler())
                .observeOn(new ComputationScheduler())
                .subscribe(
                        i -> System.out.println("Received... " + i),
                        Throwable::printStackTrace,
                        () -> System.out.println("Completed")
                );
        Thread.sleep(2000);

    }

}
