package com.noscompany.message.publisher;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.noscompany.message.publisher.VoidResult.VOID_RESULT;

@Slf4j
@RequiredArgsConstructor
final class GenericTypeUnwrapper {
    private final String subscriberName;

    Option<Object> unwrap(Object result) {
        if (result == null) {
            log.debug("{} processed the message with a result: null", subscriberName);
            return Option.none();
        } else if (result == VOID_RESULT) {
            log.debug("{} processed the message, no result (void method)", subscriberName);
            return Option.none();
        } else if (result instanceof Either either) {
            log.trace("{} - returned result is vavr.Either type, unwrapping...", subscriberName);
            return (Option<Object>) either.fold(this::unwrap, this::unwrap);
        } else if (result instanceof Option option) {
            log.trace("{} - returned result is vavr.Option type, unwrapping...", subscriberName);
            return option.flatMap(this::unwrap);
        } else if (result instanceof Try tryy) {
            log.trace("{} - returned result is vavr.Try type, unwrapping...", subscriberName);
            return (Option<Object>) tryy.toEither().fold(this::unwrap, this::unwrap);
        } else if (result instanceof Optional optional) {
            log.trace("{} - returned result is java.Optional type, unwrapping...", subscriberName);
            return Option.ofOptional(optional).flatMap(this::unwrap);
        } else {
            log.debug("{} processed the message with a result: {}", subscriberName, result);
            return Option.of(result);
        }
    }
}