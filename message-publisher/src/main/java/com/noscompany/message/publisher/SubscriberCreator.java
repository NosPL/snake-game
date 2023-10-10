package com.noscompany.message.publisher;

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

import static io.vavr.API.Tuple;
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
        return Option.of(new Subscriber(handlerName, messagePublisher, messageHandlersByMsgType, executorService));
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
}