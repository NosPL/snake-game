package com.noscompany.message.publisher.utils;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;

public final class NullMessagePublisher implements MessagePublisher {
    
    @Override
    public void publishMessage(Object message) {
    }

    @Override
    public void subscribe(Subscription subscription) {
    }

    @Override
    public void shutdown() {
    }
}