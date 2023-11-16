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

import static java.util.stream.Collectors.toCollection;
import static lombok.AccessLevel.PACKAGE;

@Slf4j
@RequiredArgsConstructor
final class SubscriberCreator {
    private final AtomicLong messageHandlerId = new AtomicLong(0);
    @Setter(PACKAGE)
    private MessagePublisher messagePublisher;
    private final Function<String, ExecutorService> executorServiceCreator;

    Option<Subscriber> createSubscriber(Subscription subscription) {
        if (subscription.getFunctions().isEmpty()) {
            log.trace("subscriber was not created, subscription did not have any messages");
            return Option.none();
        }
        var subscriberName = subscriberName(subscription);
        var messageHandlers = createMessageHandlers(subscription.getFunctions(), subscriberName);
        var executorService = subscription.getExecutorService().getOrElse(executorServiceCreator.apply(subscriberName));
        return Option.of(new Subscriber(subscriberName, messagePublisher, messageHandlers, executorService));
    }

    private String subscriberName(Subscription subscription) {
        return subscription
                .getSubscriberName()
                .getOrElse("subscriber-" + messageHandlerId.getAndIncrement());
    }

    private Collection<MessageHandler> createMessageHandlers(Map<Class, Function> functions, String subscriberName) {
        return functions
                .entrySet().stream()
                .map(entry -> MessageHandler.instance(entry.getKey(), entry.getValue(), subscriberName))
                .collect(toCollection(LinkedList::new));
    }
}