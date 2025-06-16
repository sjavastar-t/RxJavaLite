package ru.mail.kievsan.rx.core;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Механизм отмены подписки
 */
public class Disposable {
    private final AtomicBoolean disposed = new AtomicBoolean(false);

    /**
     * Отменяет подписку и прекращает доставку событий
     */
    public void dispose() {
        disposed.set(true);
    }

    /**
     * Проверяет, отменена ли подписка
     *
     * @return true, если уже отменено
     */
    public boolean isDisposed() {
        return disposed.get();
    }
}
