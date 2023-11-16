package com.noscompany.message.publisher;

import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.noscompany.message.publisher.VoidResult.VOID_RESULT;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@AllArgsConstructor(access = PRIVATE)
final class MessageHandler {
    private final Class<?> handledMessageType;
    private final Function<Object, Object> function;
    private final GenericTypeUnwrapper genericTypeUnwrapper;
    private final String subscriberName;

    static MessageHandler instance(Class messageType, Function function, String subscriberName) {
        var genericTypeUnwrapper = new GenericTypeUnwrapper(subscriberName);
        return new MessageHandler(messageType, function, genericTypeUnwrapper, subscriberName);
    }

    Option<Object> processMessage(Object message, MsgAuthorDetails msgAuthorDetails) {
        log.trace("{} is processing the message...", subscriberName);
        if (!acceptsMessageType(message.getClass())) {
            logUnsupportedMessageType(message.getClass());
            return Option.none();
        }
        try {
            Object result = function.apply(message);
            return genericTypeUnwrapper.unwrap(result);
        } catch (Throwable t) {
            logProcessMessageError(msgAuthorDetails, t);
            return Option.of(t);
        }
    }

    private void logProcessMessageError(MsgAuthorDetails msgAuthorDetails, Throwable t) {
        log.warn("{} threw an exception: ", subscriberName, t);
        log.warn("{} - message author thread name: {}", subscriberName, msgAuthorDetails.getThreadName());
        log.warn("{} - message author stack trace: ", subscriberName, msgAuthorDetails.getStackTrace());
    }

    private void logUnsupportedMessageType(Class<?> incomingMessageType) {
        log.warn("{} - wrong handler! it doesn't process message of given type", subscriberName);
        log.warn("{} - expected type: {}", subscriberName, handledMessageType);
        log.warn("{} - actual type: {}", subscriberName, incomingMessageType);
    }

    boolean acceptsMessageType(Class<?> incomingMessageType) {
        return handledMessageType.isAssignableFrom(incomingMessageType);
    }

    static <T> Function<T, VoidResult> toFunction(Consumer<T> consumer) {
        return (T t) -> {
            consumer.accept(t);
            return VOID_RESULT;
        };
    }
}