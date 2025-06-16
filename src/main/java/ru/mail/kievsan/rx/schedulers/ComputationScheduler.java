package ru.mail.kievsan.rx.schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Планировщик вычислений (fixed thread pool)
 */
public class ComputationScheduler implements Scheduler {
    private static final int CORES = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService EXEC = Executors.newFixedThreadPool(CORES);

    @Override
    public void schedule(Runnable task) {
        EXEC.submit(task);
    }
}

