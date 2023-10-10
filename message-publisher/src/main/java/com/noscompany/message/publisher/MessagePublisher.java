package com.noscompany.message.publisher;

public interface MessagePublisher {

    void publishMessage(Object message);

    void subscribe(Subscription subscription);

    void shutdown();
}