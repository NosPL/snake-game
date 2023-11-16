package com.noscompany.message.publisher;

import lombok.NonNull;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class MessagePublisherCreator {
    private final AtomicLong subscriberThreadId;
    private ExecutorService publisherExecutorService;
    private Function<String, ExecutorService> subscriberExecutorCreator;

    public MessagePublisherCreator() {
        this.subscriberThreadId = new AtomicLong(0);
        this.publisherExecutorService = defaultExecutorServiceForPublisher();
        this.subscriberExecutorCreator = this::defaultExecutorForSubscribers;
    }

    public MessagePublisherCreator executorServiceForPublisher(@NonNull ExecutorService executorService) {
        this.publisherExecutorService = executorService;
        return this;
    }

    public MessagePublisherCreator executorServiceForSubscribers(@NonNull ExecutorService executorService) {
        this.subscriberExecutorCreator = str -> executorService;
        return this;
    }

    public MessagePublisherCreator executorServicesForSubscribers(@NonNull Supplier<ExecutorService> executorServiceSupplier) {
        this.subscriberExecutorCreator = str -> executorServiceSupplier.get();
        return this;
    }

    public MessagePublisherCreator synchronous() {
        var synchronousExecutorService = new SynchronousExecutorService();
        this.publisherExecutorService = synchronousExecutorService;
        this.subscriberExecutorCreator = str -> synchronousExecutorService;
        return this;
    }

    public MessagePublisher create() {
        var subscriberCreator = new SubscriberCreator(subscriberExecutorCreator);
        var messagePublisher = new MessagePublisherImpl(new LinkedList<>(), subscriberCreator, publisherExecutorService);
        subscriberCreator.setMessagePublisher(messagePublisher);
        return messagePublisher;
    }

    private ExecutorService defaultExecutorServiceForPublisher() {
        return new ThreadPoolExecutor(
                1,
                1,
                0L, MILLISECONDS,
                new LinkedBlockingQueue<>(1000),
                r -> new Thread(r, "message-publisher-thread"));
    }

    private ExecutorService defaultExecutorForSubscribers(String subscriberName) {
        return new ThreadPoolExecutor(
                1,
                1,
                0L, MILLISECONDS,
                new LinkedBlockingQueue<>(1000),
                r -> new Thread(r, subscriberName + " thread"));
    }
}