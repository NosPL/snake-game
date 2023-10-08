package com.noscompany.snake.game.online.contract.messages;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class UserId {
    String id;

    public static UserId random() {
        return new UserId(UUID.randomUUID().toString());
    }
}