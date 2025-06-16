package ru.mail.kievsan.rx.core;


/**
 * Описывает логику эмиссии
 *
 * @param <T> тип эмитируемых элементов
 */
@FunctionalInterface
public interface OnSubscribe<T> {
    /**
     * Вызывается при подписке наблюдателя на реактивный поток
     *
     * @param observer целевой наблюдатель
     */
    void subscribe(Observer<? super T> observer);
}

