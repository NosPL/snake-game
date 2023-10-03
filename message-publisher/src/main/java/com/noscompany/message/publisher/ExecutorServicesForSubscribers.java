package com.noscompany.message.publisher;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

final class ExecutorServicesForSubscribers {
    private final List<ExecutorService> executorServices;
    private int lastGivenExecutorIndex;

    ExecutorServicesForSubscribers() {
        this.executorServices = createExecutorServices();
        lastGivenExecutorIndex = 0;
    }

    ExecutorService getNextExecutor() {
        if (lastGivenExecutorIndex >= executorServices.size()) {
            lastGivenExecutorIndex = 0;
        }
        return executorServices.get(lastGivenExecutorIndex++);
    }

    private List<ExecutorService> createExecutorServices() {
        int numberOfExecutors = calculateNumberOfExecutors();
        return IntStream
                .range(0, numberOfExecutors)
                .mapToObj(this::createThreadPool)
                .toList();
    }

    private int calculateNumberOfExecutors() {
        return Runtime.getRuntime().availableProcessors() * 2 - 1;
    }

    private ExecutorService createThreadPool(int i) {
        return new ThreadPoolExecutor(
                1,
                1,
                0L, MILLISECONDS,
                new LinkedBlockingQueue<>(10_000),
                r -> new Thread(r, "subscriber-thread-" + i));
    }
}
