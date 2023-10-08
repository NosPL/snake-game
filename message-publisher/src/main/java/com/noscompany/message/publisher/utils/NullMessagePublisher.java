package com.noscompany.message.publisher.utils;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import io.vavr.control.Option;

public final class NullMessagePublisher implements MessagePublisher {
    
    @Override
    public Option<Throwable> publishMessage(Object message) {
        return null;
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