package com.noscompany.snake.game.online.contract.messages.user.registry;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class UserName {
    String name;

    public static UserName random() {
        return new UserName(UUID.randomUUID().toString());
    }
}