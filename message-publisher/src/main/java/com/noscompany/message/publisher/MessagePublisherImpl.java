package com.noscompany.message.publisher;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@AllArgsConstructor
final class MessagePublisherImpl implements MessagePublisher {
    private final List<Subscriber> subscribers;
    private final SubscriberCreator subscriberCreator;
    private final ExecutorService executorService;

    @Override
    public void publishMessage(@NonNull Object message) {
        if (executorService.isShutdown()) {
            log.trace("publisher is shutdown, ignoring message: {}", message);
            return;
        }
        try {
            log.trace("received a message: {}, putting it in queue", message);
            var methodCaller = new MethodCaller();
            executorService.submit(() ->
                    subscribers
                            .forEach(handler -> handler.processMsg(message, methodCaller)));
        } catch (Throwable t) {
            log.error("Failed to put message in queue, reason: ", t);
        }
    }

    @Override
    public void subscribe(@NonNull Subscription subscription) {
        try {
            log.trace("putting new subscription in queue: {}", subscription);
            executorService.submit(() -> {
                log.trace("creating new subscriber: {}", subscription);
                subscriberCreator
                        .createSubscriber(subscription)
                        .peek(subscribers::add)
                        .peek(handler -> log.trace("new subscriber created"));
            });
        } catch (Throwable t) {
            log.error("Failed to add new subscription, reason: ", t);
        }
    }

    @Override
    public void shutdown() {
        try {
            log.trace("shutting down executor service");
            executorService.submit(() -> {
                subscribers.forEach(Subscriber::shutdown);
            });
            executorService.shutdown();
        } catch (Throwable t) {
            log.error("Failed to shutdown message publisher, reason: ", t);
        }
    }
}