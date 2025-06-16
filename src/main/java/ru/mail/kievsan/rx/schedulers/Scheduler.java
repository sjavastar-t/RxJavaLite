package ru.mail.kievsan.rx.schedulers;

import java.io.Closeable;

/**
 * Планировщик задач.
 */
public interface Scheduler extends Closeable {
    /**
     * Запланировать выполнение задачи.
     *
     * @param task Runnable-задание
     */
    void schedule(Runnable task);
}

