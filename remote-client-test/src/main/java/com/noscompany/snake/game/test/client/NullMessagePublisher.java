package com.noscompany.snake.game.test.client;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import io.vavr.control.Option;

final class NullMessagePublisher implements MessagePublisher {

    @Override
    public Option<Throwable> publishMessage(Object message) {
        return Option.none();
    }

    @Override
    public Option<Throwable> subscribe(Subscription subscription) {
        return null;
    }

    @Override
    public Option<Throwable> shutdown() {
        return null;
    }
}