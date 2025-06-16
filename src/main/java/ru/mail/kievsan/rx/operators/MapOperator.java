package ru.mail.kievsan.rx.operators;

import ru.mail.kievsan.rx.core.Observable;
import ru.mail.kievsan.rx.core.Observer;

import java.util.function.Function;


/**
 * Оператор применения заданной функцию к каждому элементу потока
 */
public class MapOperator {
    public static <T, R> Observable<R> apply(
            Observable<T> source,
            Function<? super T, ? extends R> mapper
    ) {
        return Observable.create(observer -> {
            source.subscribe(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    observer.onNext(mapper.apply(item));
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

