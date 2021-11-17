package com.noscompany.snake.game.server.message.sender;

import org.atmosphere.cpr.BroadcasterFactory;

public class MessageSenderCreator {

    public static MessageSender messageSender(BroadcasterFactory broadcasterFactory) {
        return new MessageSenderImpl(broadcasterFactory);
    }
}