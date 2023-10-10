package com.noscompany.message.publisher;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toCollection;
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
        var messageHandlers = createMessageHandlers(subscription.getFunctions(), handlerName);
        var executorService = subscription.getExecutorService().getOrElse(executorServiceSupplier);
        return Option.of(new Subscriber(handlerName, messagePublisher, messageHandlers, executorService));
    }

    private String handlerName(Subscription subscription) {
        return subscription
                .getSubscriberName()
                .getOrElse("subscriber-" + messageHandlerId.getAndIncrement());
    }

    private Collection<MessageHandler> createMessageHandlers(Map<Class, Function> functions, String handlerName) {
        return functions
                .entrySet().stream()
                .map(entry -> new MessageHandler(entry.getKey(), entry.getValue(), handlerName))
                .collect(toCollection(LinkedList::new));
    }
}