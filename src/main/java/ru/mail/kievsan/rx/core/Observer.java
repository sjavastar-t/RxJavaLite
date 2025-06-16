package ru.mail.kievsan.rx.core;


/**
 * Наблюдатель реактивного потока данных
 *
 * @param <T> тип данных - элементов эмиссии
 */
public interface Observer<T> {
    /**
     * Вызывается при поступлении нового элемента.
     *
     * @param item элемент потока
     */
    void onNext(T item);

    /**
     * Вызывается при ошибке в потоке.
     *
     * @param t возникшая ошибка
     */
    void onError(Throwable t);

    /**
     * Вызывается при завершении потока.
     */
    void onComplete();
}

