package com.noscompany.message.publisher;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

final class SynchronousExecutorService implements ExecutorService {
    private boolean isShutdown = false;

    @Override
    public void shutdown() {
        isShutdown = true;
    }

    @Override
    public List<Runnable> shutdownNow() {
        return new LinkedList<>();
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }

    @Override
    public boolean isTerminated() {
        return isShutdown;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return true;
    }

    @Override
    @SneakyThrows
    public <T> Future<T> submit(Callable<T> task) {
        return new FinishedFuture<>(task.call());
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        task.run();
        return new FinishedFuture<>(result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        task.run();
        return new FinishedFuture<>(null);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return tasks
                .stream()
                .map(this::executeCallable)
                .map(FinishedFuture::new)
                .map(finishedFuture -> (Future<T>) finishedFuture)
                .toList();
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return invokeAll(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return tasks
                .stream()
                .findFirst()
                .map(this::executeCallable)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return tasks
                .stream()
                .findFirst()
                .map(this::executeCallable)
                .orElseThrow(RuntimeException::new);
    }

    @SneakyThrows
    private <T> T executeCallable(Callable<T> callable) {
        return callable.call();
    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }

    @AllArgsConstructor
    private static final class FinishedFuture<T> implements Future<T> {
        private final T result;

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            return result;
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return result;
        }
    }
}