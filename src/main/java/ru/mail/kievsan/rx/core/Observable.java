package ru.mail.kievsan.rx.core;

import lombok.extern.slf4j.Slf4j;
import ru.mail.kievsan.rx.schedulers.Scheduler;

import lombok.NonNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.IntStream;


/**
 * Основной класс реактивного потока.
 *
 * @param <T> тип элементов
 */
@Slf4j
public class Observable<T> {

    private final OnSubscribe<T> source;

    private Observable(OnSubscribe<T> source) {
        this.source = source;
    }

    /**
     * Фабричный метод
     *
     * @param source логика эмиссии элементов
     * @param <T>    тип элементов
     * @return новый Observable
     */
    public static <T> Observable<T> create(OnSubscribe<T> source) {
        log.debug("Создание Observable: create()");
        return new Observable<>(source);
    }

    /**
     * Фабричный метод для источника эмиссии с одним элементом
     *
     * @param item элемент
     * @param <T>  тип элемента
     * @return Observable, эмитирующий один элемент и завершающийся
     */
    public static <T> Observable<T> just(@NonNull T item) {
        Objects.requireNonNull(item, "item is null");
        return create(observer -> {
            observer.onNext(item);
            observer.onComplete();
        });
    }

    /**
     * Фабричный метод для набора элементов
     *
     * @param items элементы для эмиссии
     * @param <T>   тип элементов
     * @return новый Observable
     */
    @SafeVarargs
    public static <T> Observable<T> just(T... items) {
        return create(observer -> {
            Arrays.stream(items).forEach(observer::onNext);
            observer.onComplete();
        });
    }

    /**
     * Фабричный метод для последовательного интервала целых чисел
     *
     * @param start     первый элемент эмиссии
     * @param count     количество элементов
     * @return новый Observable
     */
    public static Observable<Integer> range(int start, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count >= 0 required but it was " + count);
        } else if (count == 0) {
            return null;
        } else if (count == 1) {
            return just(start);
        } else if ((long)start + (long)(count - 1) > 2147483647L) {
            throw new IllegalArgumentException("Integer overflow");
        } else {
            return create(observer -> {
                IntStream.range(start, start + count).boxed().forEach(observer::onNext);
                observer.onComplete();
            });
        }
    }

    /**
     * Подписка с полным набором обработчиков
     *
     * @param onNext     действие при новом элементе
     * @param onError    действие при ошибке
     * @param onComplete действие при завершении
     * @return Disposable для отмены подписки
     */
    public Disposable subscribe(
            Consumer<? super T> onNext,
            Consumer<Throwable> onError,
            Runnable onComplete
    ) {
        Observer<T> obs = new Observer<T>() {
            @Override public void onNext(T item)     { onNext.accept(item); }
            @Override public void onError(Throwable t) { onError.accept(t); }
            @Override public void onComplete()       { onComplete.run(); }
        };
        return subscribe(obs);
    }

    /**
     * Подписка с обработчиком onNext
     *
     * @param onNext действие при новом элементе
     * @return RxDisposable для отмены подписки
     */
    public Disposable subscribe(Consumer<? super T> onNext) {
        return subscribe(onNext, Throwable::printStackTrace, () -> {});
    }

    /**
     * Базовый subscribe, возвращает Disposable
     *
     * @param observer наблюдатель
     * @return Disposable для отмены подписки
     */
    public Disposable subscribe(Observer<? super T> observer) {
        log.debug("Новая подписка на RxObservable");
        Disposable disposable = new Disposable();
        try {
            source.subscribe(new Observer<T>() {
                @Override
                public void onNext(T item) {
                    if (!disposable.isDisposed()) {
                        observer.onNext(item);
                    }
                }
                @Override
                public void onError(Throwable t) {
                    if (!disposable.isDisposed()) {
                        observer.onError(t);
                    }
                }
                @Override
                public void onComplete() {
                    if (!disposable.isDisposed()) {
                        observer.onComplete();
                    }
                }
            });
        } catch (Throwable t) {
            observer.onError(t);
        }
        return disposable;
    }

    /**
     * Подписка выполняется в указанном планировщике
     *
     * @param scheduler планировщик для вызова source.subscribe
     * @return новый Observable, подписка которого отложена на scheduler
     */
    public Observable<T> subscribeOn(Scheduler scheduler) {
        return Observable.create(observer ->
                scheduler.schedule(() -> this.subscribe(observer))
        );
    }

    /**
     * Эмиссия onNext/onError/onComplete происходит в заданном планировщике
     *
     * @param scheduler планировщик для обработки событий
     * @return новый Observable, события переключаются на scheduler
     */
    public Observable<T> observeOn(Scheduler scheduler) {
        return Observable.create(observer ->
                this.subscribe(new Observer<T>() {
                    @Override
                    public void onNext(T item) {
                        scheduler.schedule(() -> observer.onNext(item));
                    }
                    @Override
                    public void onError(Throwable t) {
                        scheduler.schedule(() -> observer.onError(t));
                    }
                    @Override
                    public void onComplete() {
                        scheduler.schedule(observer::onComplete);
                    }
                })
        );
    }
}