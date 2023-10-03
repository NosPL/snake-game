package com.noscompany.message.publisher;

import io.vavr.control.Option;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.noscompany.message.publisher.MessageHandler.toFunction;
import static lombok.AccessLevel.PACKAGE;

@NoArgsConstructor
public final class Subscription {
    @Getter(PACKAGE)
    private final Map<Class, Function> functions = new HashMap<>();
    @Getter(PACKAGE)
    private Option<ExecutorService> executorService = Option.none();
    @Getter(PACKAGE)
    private Option<String> subscriberName = Option.none();

    public boolean containsMessageType(@NonNull Class clazz) {
        return functions.containsKey(clazz);
    }

    public <T> Subscription toMessage(@NonNull Class<T> messageType, @NonNull Consumer<T> consumer) {
        functions.put(messageType, toFunction(consumer));
        return this;
    }

    public <T> Subscription toMessage(@NonNull Class<T> messageType, @NonNull Function<T, Object> function) {
        functions.put(messageType, function);
        return this;
    }

    public Subscription executorService(@NonNull ExecutorService executorService) {
        this.executorService = Option.of(executorService);
        return this;
    }

    public Subscription subscriberName(@NonNull String subscriberName) {
        this.subscriberName = Option.of(subscriberName);
        return this;
    }

    @Override
    public String toString() {
        return ToStringFormatter.toString(this);
    }

    private static final class ToStringFormatter {

        static private String toString(Subscription subscription) {
            return subscriberName(subscription) + ", " + executorService(subscription) + ", " + messages(subscription);
        }

        static private String subscriberName(Subscription subscription) {
            return "subscriber name: " +
                    subscription.getSubscriberName().getOrElse("not defined");
        }

        static private String executorService(Subscription subscription) {
            return "custom executor service: " +
                    (subscription.getExecutorService().isDefined() ? "defined" : "not defined");
        }

        static private String messages(Subscription subscription) {
            return "subscribed messages: " +
                    subscription
                            .functions.keySet().stream()
                            .map(Class::getName)
                            .reduce((acc, nextArg) -> acc + nextArg + ", ")
                            .orElse("none");
        }
    }
}