package com.noscompany.message.publisher;

import lombok.NonNull;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class MessagePublisherCreator {
    private ExecutorService publisherExecutorService;
    private Supplier<ExecutorService> subscriberExecutorSupplier;

    public MessagePublisherCreator() {
        this.publisherExecutorService = defaultExecutorServiceForPublisher();
        this.subscriberExecutorSupplier = () -> defaultExecutorServiceForSubscribers();
    }

    public MessagePublisherCreator executorServiceForPublisher(@NonNull ExecutorService executorService) {
        this.publisherExecutorService = executorService;
        return this;
    }

    public MessagePublisherCreator executorServiceForSubscribers(@NonNull ExecutorService executorService) {
        this.subscriberExecutorSupplier = () -> executorService;
        return this;
    }

    public MessagePublisherCreator executorServicesForSubscribers(@NonNull Supplier<ExecutorService> executorServiceSupplier) {
        this.subscriberExecutorSupplier = executorServiceSupplier;
        return this;
    }

    public MessagePublisherCreator synchronous() {
        var synchronousExecutorService = new SynchronousExecutorService();
        this.publisherExecutorService = synchronousExecutorService;
        this.subscriberExecutorSupplier = () -> synchronousExecutorService;
        return this;
    }

    public MessagePublisher create() {
        var subscriberCreator = new SubscriberCreator(subscriberExecutorSupplier);
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

    private ExecutorService defaultExecutorServiceForSubscribers() {
        return new ThreadPoolExecutor(
                1,
                1,
                0L, MILLISECONDS,
                new LinkedBlockingQueue<>(1000),
                r -> new Thread(r, "message-publisher-thread"));
    }
}