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

    Option<Object> processMessage(Object message, MethodCaller methodCaller) {
        if (!acceptsMessageType(message.getClass())) {
            return Option.none();
        }
        try {
            Object result = function.apply(message);
            return flatten(result);
        } catch (Throwable t) {
            log.debug("{} threw an exception: ", subscriberName, t);
            log.debug("{} - message author thread name: {}", subscriberName, methodCaller.getThreadName());
            log.debug("{} - message author stack trace: ", subscriberName, methodCaller.getStackTrace());
            return Option.of(t);
        }
    }

    boolean acceptsMessageType(Class<?> incomingMessageType) {
        return handledMessageType.isAssignableFrom(incomingMessageType);
    }

    private Option<Object> flatten(Object result) {
        if (result == null) {
            log.trace("function result is null");
            return Option.none();
        } else if (result instanceof Either either) {
            log.trace("function result is vavr.Either type, flattening...");
            return (Option<Object>) either.fold(this::flatten, this::flatten);
        } else if (result instanceof Option option) {
            log.trace("function result is vavr.Option type, flattening...");
            return option.flatMap(this::flatten);
        } else if (result instanceof Try tryy) {
            log.trace("function result is vavr.Try type, flattening...");
            return (Option<Object>) tryy.toEither().fold(this::flatten, this::flatten);
        } else if (result instanceof Optional optional) {
            log.trace("function result is java.Optional type, flattening...");
            return Option.ofOptional(optional).flatMap(this::flatten);
        } else {
            log.trace("function result type: {}", result.getClass());
            return Option.of(result);
        }
    }

    static <T> Function<T, Option> toFunction(Consumer<T> consumer) {
        return (T t) -> {
            consumer.accept(t);
            return Option.none();
        };
    }
}