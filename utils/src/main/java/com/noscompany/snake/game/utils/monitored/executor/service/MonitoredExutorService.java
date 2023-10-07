package com.noscompany.snake.game.utils.monitored.executor.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@AllArgsConstructor
class MonitoredExutorService implements ExecutorService {
    private final BlockingQueue<Runnable> queue;
    private final ExecutorService executorService;
    private final RejectedExecutionHandler rejectedExecutionHandler;

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(wrapWithLogs(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return executorService.submit(wrapWithLogs(task), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        try {
            return executorService.submit(wrapWithLogs(task));
        }catch (RejectedExecutionException e) {
            log.error("Failed to submit task: '{}', cause: ", task, e);
            if (rejectedExecutionHandler instanceof ThreadPoolExecutor.AbortPolicy)
                throw e;
            return null;
        }
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return executorService.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return executorService.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return executorService.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        executorService.execute(command);
    }

    private <T> Callable<T> wrapWithLogs(Callable<T> task) {
        Exception clientStack = new Exception("Client stack trace");
        long putToQueueTime = System.currentTimeMillis();
        String clientThreadName = Thread.currentThread().getName();
        return () -> {
            log.debug("took out task: '{}' from queue submitted by thread: '{}'", task, clientThreadName);
            log.debug("number of tasks in queue: {}", queue.size());
            long timeInQueue = System.currentTimeMillis() - putToQueueTime;
            log.debug("task '{}' spent {} ms in queue", task, timeInQueue);
            log.debug("starting task");
            try {
                long startTaskTime = System.currentTimeMillis();
                T result = task.call();
                long taskFinishedTime = System.currentTimeMillis() - startTaskTime;
                log.debug("Task: '{}' finished in {} ms", task, taskFinishedTime);
                return result;
            } catch (Exception e) {
                log.error("Exception {} got thrown by task '{}' submitted by thread {} from here: ", e, task, clientThreadName, clientStack);
                throw e;
            }
        };
    }

    private Runnable wrapWithLogs(Runnable task) {
        log.debug("queue size before putting new task: {}", queue.size());
        Exception clientStack = new Exception("Client stack trace");
        long startTime = System.currentTimeMillis();
        String clientThreadName = Thread.currentThread().getName();
        return wrapWithLogs(task, clientStack, startTime, clientThreadName);
    }

    private Runnable wrapWithLogs(Runnable task, Exception clientStack, long putToQueueTime, String clientThreadName) {
        return () -> {
            log.debug("took out task: '{}' from queue submitted by thread: '{}'", task, clientThreadName);
            log.debug("number of tasks in queue: {}", queue.size());
            long timeInQueue = System.currentTimeMillis() - putToQueueTime;
            log.debug("task '{}' spent {} ms in queue", task, timeInQueue);
            log.debug("starting task");
            try {
                long startTaskTime = System.currentTimeMillis();
                task.run();
                long taskFinishedTime = System.currentTimeMillis() - startTaskTime;
                log.debug("Task: '{}' finished in {} ms", task, taskFinishedTime);
            } catch (Exception e) {
                log.error("Exception thrown: ", e);
                log.error("Thrown by task '{}'", task);
                log.error("Submitted by thread {} from here: ",clientThreadName, clientStack);
                throw e;
            }
        };
    }
}
