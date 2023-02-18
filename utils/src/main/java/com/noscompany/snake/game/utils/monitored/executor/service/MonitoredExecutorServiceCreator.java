package com.noscompany.snake.game.utils.monitored.executor.service;

import java.util.concurrent.*;

public class MonitoredExecutorServiceCreator {

    public ExecutorService create(int corePoolSize, int maxPoolSize, BlockingQueue<Runnable> queue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        ExecutorService executorService = executorService(corePoolSize, maxPoolSize, queue, threadFactory, new ThreadPoolExecutor.AbortPolicy());
        return new MonitoredExutorService(queue, executorService, rejectedExecutionHandler);
    }

    public ExecutorService create() {
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(100);
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();
        ExecutorService executorService = executorService(2, 2, queue, threadFactory(), new ThreadPoolExecutor.AbortPolicy());
        return new MonitoredExutorService(queue, executorService, rejectedExecutionHandler);
    }

    private ExecutorService executorService(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, ThreadPoolExecutor.AbortPolicy rejectionHandler) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                0L, TimeUnit.MILLISECONDS,
                workQueue,
                threadFactory,
                rejectionHandler);
    }

    private ThreadFactory threadFactory() {
        return runnable -> {
            Thread thread = new Thread(runnable);
            return thread;
        };
    }
}