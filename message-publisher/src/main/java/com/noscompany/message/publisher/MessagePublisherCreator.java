package com.noscompany.message.publisher;

import com.codahale.metrics.InstrumentedExecutorService;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.Slf4jReporter;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

import static com.codahale.metrics.Slf4jReporter.LoggingLevel.DEBUG;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class MessagePublisherCreator {
    private ExecutorService publisherExecutorService;
    private Supplier<ExecutorService> subscriberExecutorSupplier;

    public MessagePublisherCreator() {
        this.publisherExecutorService = defaultExecutorServiceForPublisher();
        var executorServicesPool = new ExecutorServicesForSubscribers();
        this.subscriberExecutorSupplier = executorServicesPool::getNextExecutor;
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
        var metricRegistry = SharedMetricRegistries.getOrCreate("message publisher");
        publisherExecutorService = new InstrumentedExecutorService(publisherExecutorService, metricRegistry, "message publisher");
        var reporter = createReporter(metricRegistry);
        var messagePublisher = new MessagePublisherImpl(new LinkedList<>(), subscriberCreator, publisherExecutorService, reporter);
        subscriberCreator.setMessagePublisher(messagePublisher);
        return messagePublisher;
    }

    private ExecutorService defaultExecutorServiceForPublisher() {
        return new ThreadPoolExecutor(
                1,
                1,
                0L, MILLISECONDS,
                new LinkedBlockingQueue<>(10_000),
                r -> new Thread(r, "message-publisher-thread"));
    }

    private Slf4jReporter createReporter(MetricRegistry metricRegistry) {
        return Slf4jReporter
                .forRegistry(metricRegistry)
                .withLoggingLevel(DEBUG)
                .convertDurationsTo(MICROSECONDS)
                .convertRatesTo(MILLISECONDS)
                .build();
    }
}