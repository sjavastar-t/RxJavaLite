package ru.mail.kievsan.rx.schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Планировщик I/O-потоков (cached thread pool)
 */
public class IOScheduler implements Scheduler {
    private static final ExecutorService EXEC = Executors.newCachedThreadPool();

    @Override
    public void schedule(Runnable task) {
        EXEC.submit(task);
    }

    public void close() {
        EXEC.close();
    }
}

