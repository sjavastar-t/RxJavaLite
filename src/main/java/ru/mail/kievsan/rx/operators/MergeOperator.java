package ru.mail.kievsan.rx.operators;

import ru.mail.kievsan.rx.core.CompositeDisposable;
import ru.mail.kievsan.rx.core.Disposable;
import ru.mail.kievsan.rx.core.Observable;
import ru.mail.kievsan.rx.core.Observer;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Оператор параллельной свертки нескольких Observable в один поток
 */
public class MergeOperator {

    /**
     * @param sources набор Observable-источников
     * @param <T>     тип элементов
     * @return новый Observable<T>, эмитирующий все элементы sources
     */
    @SafeVarargs
    public static <T> Observable<T> apply(Observable<? extends T>... sources) {
        return Observable.create(observer -> {
            CompositeDisposable composite = new CompositeDisposable();
            AtomicInteger remaining = new AtomicInteger(sources.length);
            ConcurrentLinkedQueue<Throwable> errors = new ConcurrentLinkedQueue<>();

            for (Observable<? extends T> src : sources) {
                Disposable disp = src.subscribe(new Observer<T>() {
                    @Override
                    public void onNext(T item) {
                        observer.onNext(item);
                    }

                    @Override
                    public void onError(Throwable t) {
                        errors.add(t);
                        completeIfDone();
                    }

                    @Override
                    public void onComplete() {
                        completeIfDone();
                    }

                    private void completeIfDone() {
                        if (remaining.decrementAndGet() == 0) {
                            Throwable err = errors.poll();
                            if (err != null) {
                                observer.onError(err);
                            } else {
                                observer.onComplete();
                            }
                            composite.dispose();
                        }
                    }
                });
                composite.add(disp);
            }
        });
    }
}
