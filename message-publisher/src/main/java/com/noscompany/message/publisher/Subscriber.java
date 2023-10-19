package com.noscompany.message.publisher;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import static lombok.AccessLevel.PACKAGE;

@Slf4j
@AllArgsConstructor(access = PACKAGE)
final class Subscriber {
    @Getter
    private final String subscriberName;
    private final MessagePublisher messagePublisher;
    private final Collection<MessageHandler> handlersByMsgType;
    private final ExecutorService executorService;

    void processMsg(Object message, MsgAuthorDetails msgAuthorDetails) {
        if (executorService.isShutdown()) {
            log.trace("{} is shutdown, ignoring message: {}",subscriberName, message);
            return;
        }
        try {
            executorService.submit(() -> tryToProcessMessage(message, msgAuthorDetails));
        } catch (RejectedExecutionException t) {
            log.error("{} failed to put the message in queue, reason: ", subscriberName, t);
            log.error("{} - message author thread: {}, author stack trace: ", subscriberName, msgAuthorDetails.getThreadName(), msgAuthorDetails.getStackTrace());
        }
    }

    private void tryToProcessMessage(Object message, MsgAuthorDetails msgAuthorDetails) {
        findHandler(message.getClass())
                .peek(handler -> log.trace("{} received a message: {}, type: {}", subscriberName, message, message.getClass().getName()))
                .flatMap(handler -> handler.processMessage(message, msgAuthorDetails))
                .peek(result -> log.trace("{} passes the result to the message publisher...", subscriberName))
                .peek(messagePublisher::publishMessage);
    }

    private Option<MessageHandler> findHandler(Class<?> messageType) {
        return handlersByMsgType
                .stream()
                .filter(messageHandler -> messageHandler.acceptsMessageType(messageType))
                .findFirst()
                .map(Option::of).orElse(Option.none());
    }

    void shutdown() {
        try {
            log.trace("{} - shutting down executor service...", subscriberName);
            executorService.shutdown();
        } catch (Throwable t) {
            log.error("{} - failed to shutdown executor service, reason: ", subscriberName, t);
        }
    }
}