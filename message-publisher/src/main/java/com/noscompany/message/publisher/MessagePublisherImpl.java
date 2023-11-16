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
            log.trace("publisher received a message: {}, passing it to the subscribers", message);
            var msgAuthorDetails = new MsgAuthorDetails();
            executorService.submit(() ->
                    subscribers
                            .forEach(handler -> handler.processMsg(message, msgAuthorDetails)));
        } catch (Throwable t) {
            log.error("Failed to put message in queue, reason: ", t);
        }
    }

    @Override
    public void subscribe(@NonNull Subscription subscription) {
        if (executorService.isShutdown()) {
            log.trace("publisher is shutdown, ignoring subscription: {}", subscription);
            return;
        }
        try {
            executorService.submit(() -> {
                log.trace("publisher is creating new subscriber: {}", subscription);
                subscriberCreator
                        .createSubscriber(subscription)
                        .peek(subscribers::add)
                        .peek(subscriber -> log.debug("publisher added new subscriber with name: {}", subscriber.getSubscriberName()));
            });
        } catch (Throwable t) {
            log.error("publisher failed to add new subscription, reason: ", t);
        }
    }

    @Override
    public void shutdown() {
        try {
            log.trace("publisher is shutting down executor service");
            executorService.submit(() -> {
                subscribers.forEach(Subscriber::shutdown);
            });
            executorService.shutdown();
        } catch (Throwable t) {
            log.error("Failed to shutdown message publisher, reason: ", t);
        }
    }
}