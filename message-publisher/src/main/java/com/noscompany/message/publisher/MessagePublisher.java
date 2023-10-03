package com.noscompany.message.publisher;

import io.vavr.control.Option;

public interface MessagePublisher {

    Option<Throwable> publishMessage(Object message);

    Option<Throwable> subscribe(Subscription subscription);

    Option<Throwable> shutdown();
}