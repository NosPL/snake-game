package com.noscompany.message.publisher;

import com.codahale.metrics.InstrumentedExecutorService;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.Slf4jReporter;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.codahale.metrics.Slf4jReporter.LoggingLevel.DEBUG;
import static io.vavr.API.Tuple;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PACKAGE;

@Slf4j
@RequiredArgsConstructor
final class SubscriberCreator {
    private final AtomicLong messageHandlerId = new AtomicLong(0);
    @Setter(PACKAGE)
    private MessagePublisher messagePublisher;
    private final Supplier<ExecutorService> executorServiceSupplier;

    Option<Subscriber> createSubscriber(Subscription subscription) {
        if (subscription.getFunctions().isEmpty()) {
            log.warn("subscriber was not created, subscription did not have any messages");
            return Option.none();
        }
        var handlerName = handlerName(subscription);
        var messageHandlersByMsgType = createMessageHandlers(subscription.getFunctions(), handlerName);
        var executorService = subscription.getExecutorService().getOrElse(executorServiceSupplier);
        var metricRegistry = SharedMetricRegistries.getOrCreate(handlerName);
        executorService = new InstrumentedExecutorService(executorService, metricRegistry, handlerName);
        var reporter = createReporter(metricRegistry);
        return Option.of(new Subscriber(handlerName, messagePublisher, messageHandlersByMsgType, executorService, reporter));
    }

    private String handlerName(Subscription subscription) {
        return subscription
                .getSubscriberName()
                .getOrElse("subscriber-" + messageHandlerId.getAndIncrement());
    }

    private Map<Class, MessageHandler> createMessageHandlers(Map<Class, Function> functions, String handlerName) {
        return functions
                .entrySet().stream()
                .map(entry -> Tuple(entry.getKey(), new MessageHandler(entry.getValue(), handlerName)))
                .collect(toMap(Tuple2::_1, Tuple2::_2));
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