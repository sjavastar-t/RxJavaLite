package ru.mail.kievsan.rx.schedulers;

/**
 * Планировщик задач.
 */
public interface Scheduler {
    /**
     * Запланировать выполнение задачи.
     *
     * @param task Runnable-задание
     */
    void schedule(Runnable task);
}

