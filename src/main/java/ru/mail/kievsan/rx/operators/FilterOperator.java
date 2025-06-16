package ru.mail.kievsan.rx.operators;

import ru.mail.kievsan.rx.core.Observable;
import ru.mail.kievsan.rx.core.Observer;

import java.util.function.Predicate;


/**
 * Оператор фильтрации элементов, удовлетворяющих условию предиката
 */
public class FilterOperator {
    public static <T> Observable<T> apply(
            Observable<T> source,
            Predicate<? super T> predicate
    ) {
        return Observable.create(observer -> {
            source.subscribe(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    if (predicate.test(item)) {
                        observer.onNext(item);
                    }
                }
                @Override
                public void onError(Throwable t) {
                    observer.onError(t);
                }
                @Override
                public void onComplete() {
                    observer.onComplete();
                }
            });
        });
    }
}

