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
        try {
            log.debug("received a message: {}, putting it in queue", message);
            var methodCaller = new MethodCaller();
            executorService.submit(() -> {
                subscribers.stream()
                        .filter(handler -> handler.accepts(message.getClass()))
                        .forEach(handler -> handler.processMsg(message, methodCaller));
            });
        } catch (Throwable t) {
            log.error("Failed to put message in queue, reason: ", t);
        }
    }

    @Override
    public void subscribe(@NonNull Subscription subscription) {
        try {
            log.debug("putting new subscription in queue: {}", subscription);
            executorService.submit(() -> {
                log.debug("creating new subscriber: {}", subscription);
                subscriberCreator
                        .createSubscriber(subscription)
                        .peek(subscribers::add)
                        .peek(handler -> log.debug("new subscriber created"));
            });
        } catch (Throwable t) {
            log.error("Failed to add new subscription, reason: ", t);
        }
    }

    @Override
    public void shutdown() {
        try {
            log.debug("shutting down executor service");
            executorService.submit(() -> {
                subscribers.forEach(Subscriber::shutdown);
            });
            executorService.shutdown();
        } catch (Throwable t) {
            log.error("Failed to shutdown message publisher, reason: ", t);
        }
    }
}