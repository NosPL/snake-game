package com.noscompany.message.publisher;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor
@Slf4j
final class MessageHandler {
    private final Class<?> handledMessageType;
    private final Function<Object, Object> function;
    private final String subscriberName;
    private static final VoidResult VOID_RESULT = new VoidResult();

    Option<Object> processMessage(Object message, MsgAuthorDetails msgAuthorDetails) {
        log.trace("{} is processing the message...", subscriberName);
        if (!acceptsMessageType(message.getClass())) {
            log.warn("{} - wrong handler! it doesn't process message of given type", subscriberName);
            log.warn("{} - expected type: {}", subscriberName, handledMessageType);
            log.warn("{} - actual type: {}", subscriberName, handledMessageType);
            return Option.none();
        }
        try {
            Object result = function.apply(message);
            return flatten(result);
        } catch (Throwable t) {
            log.warn("{} threw an exception: ", subscriberName, t);
            log.warn("{} - message author thread name: {}", subscriberName, msgAuthorDetails.getThreadName());
            log.warn("{} - message author stack trace: ", subscriberName, msgAuthorDetails.getStackTrace());
            return Option.of(t);
        }
    }

    boolean acceptsMessageType(Class<?> incomingMessageType) {
        return handledMessageType.isAssignableFrom(incomingMessageType);
    }

    private Option<Object> flatten(Object result) {
        if (result == null) {
            log.trace("{} processed the message with a result: null", subscriberName);
            return Option.none();
        } else if (result.equals(VOID_RESULT)) {
            log.trace("{} processed the message, no result (void method)", subscriberName);
            return Option.none();
        } else if (result instanceof Either either) {
            log.trace("{} - returned result is vavr.Either type, flattening...", subscriberName);
            return (Option<Object>) either.fold(this::flatten, this::flatten);
        } else if (result instanceof Option option) {
            log.trace("{} - returned result is vavr.Option type, flattening...", subscriberName);
            return option.flatMap(this::flatten);
        } else if (result instanceof Try tryy) {
            log.trace("{} - returned result is vavr.Try type, flattening...", subscriberName);
            return (Option<Object>) tryy.toEither().fold(this::flatten, this::flatten);
        } else if (result instanceof Optional optional) {
            log.trace("{} - returned result is java.Optional type, flattening...", subscriberName);
            return Option.ofOptional(optional).flatMap(this::flatten);
        } else {
            log.trace("{} processed the message with a result: {}", subscriberName, result);
            return Option.of(result);
        }
    }

    static <T> Function<T, VoidResult> toFunction(Consumer<T> consumer) {
        return (T t) -> {
            consumer.accept(t);
            return new VoidResult();
        };
    }
}