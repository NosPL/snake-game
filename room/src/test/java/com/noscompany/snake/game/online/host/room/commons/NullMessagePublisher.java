package com.noscompany.snake.game.online.host.room.commons;

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
        return Option.none();
    }

    @Override
    public Option<Throwable> shutdown() {
        return Option.none();
    }
}