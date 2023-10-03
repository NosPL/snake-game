package com.noscompany.message.publisher;

import com.codahale.metrics.ScheduledReporter;
import io.vavr.control.Option;
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
    private final ScheduledReporter reporter;

    @Override
    public Option<Throwable> publishMessage(@NonNull Object message) {
        try {
            log.debug("received a message: {}, putting it in queue", message);
            var methodCaller = new MethodCaller();
            executorService.submit(() -> {
                subscribers.stream()
                        .filter(handler -> handler.accepts(message.getClass()))
                        .forEach(handler -> handler.processMsg(message, methodCaller));
                reporter.report();
            });
            return Option.none();
        } catch (Throwable t) {
            log.error("Failed to put message in queue, reason: ", t);
            return Option.of(t);
        }
    }

    @Override
    public Option<Throwable> subscribe(@NonNull Subscription subscription) {
        try {
            log.debug("putting new subscription in queue: {}", subscription);
            executorService.submit(() -> {
                log.debug("creating new subscriber: {}", subscription);
                subscriberCreator
                        .createSubscriber(subscription)
                        .peek(subscribers::add)
                        .peek(handler -> log.debug("new subscriber created"));
                reporter.report();
            });
            return Option.none();
        } catch (Throwable t) {
            log.error("Failed to add new subscription, reason: ", t);
            return Option.of(t);
        }
    }

    @Override
    public Option<Throwable> shutdown() {
        try {
            log.debug("shutting down executor service");
            executorService.submit(() -> {
                subscribers.forEach(Subscriber::shutdown);
            });
            executorService.shutdown();
            return Option.none();
        } catch (Throwable t) {
            log.error("Failed to shutdown message publisher, reason: ", t);
            return Option.of(t);
        }
    }
}