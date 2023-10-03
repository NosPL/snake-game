package com.noscompany.message.publisher;

import com.codahale.metrics.ScheduledReporter;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import static lombok.AccessLevel.PACKAGE;

@Slf4j
@AllArgsConstructor(access = PACKAGE)
final class Subscriber {
    @Getter
    private final String subscriberName;
    private final MessagePublisher messagePublisher;
    private final Map<Class, MessageHandler> handlersByMsgType;
    private final ExecutorService executorService;
    private final ScheduledReporter reporter;

    boolean accepts(Class messageType) {
        return handlersByMsgType.containsKey(messageType);
    }

    void processMsg(Object message, MethodCaller methodCaller) {
        try {
            executorService.submit(() -> tryToProcessMessage(message, methodCaller));
        } catch (RejectedExecutionException t) {
            log.error("{} failed to put the message in queue, reason: ", subscriberName, t);
            log.error("{} - message author thread: {}, author stack trace: ", subscriberName, methodCaller.getThreadName(), methodCaller.getStackTrace());
        }
    }

    private void tryToProcessMessage(Object message, MethodCaller methodCaller) {
        log.debug("{} received a message: {}, type: {}, looking for a handler", subscriberName, message, message.getClass().getName());
        findHandler(message.getClass())
                    .flatMap(handler -> handler.processMessage(message, methodCaller))
                    .peek(result -> log.debug("{} processed the message with a result: {}, passing the result to the message publisher...", subscriberName, result))
                    .peek(messagePublisher::publishMessage);
        reporter.report();
    }

    private Option<MessageHandler> findHandler(Class<?> messageType) {
        return Option
                .of(handlersByMsgType.get(messageType))
                .peek(messageHandler -> log.debug("{} found the handler, processing the message...", subscriberName))
                .onEmpty(() -> log.warn("{} did not find the handler", subscriberName));
    }

    void shutdown() {
        try{
            log.debug("{} - shutting down executor service...", subscriberName);
            executorService.shutdown();
        } catch (Throwable t) {
            log.error("{} - failed to shutdown executor service, reason: ", subscriberName, t);
        }
    }
}