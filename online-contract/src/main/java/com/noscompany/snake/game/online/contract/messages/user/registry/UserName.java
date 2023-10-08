package com.noscompany.snake.game.online.contract.messages.user.registry;

import lombok.Value;

import java.util.UUID;

@Value
public class UserName {
    String name;

    public static UserName random() {
        return new UserName(UUID.randomUUID().toString());
    }
}