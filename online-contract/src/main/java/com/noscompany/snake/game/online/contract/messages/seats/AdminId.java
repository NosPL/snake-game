package com.noscompany.snake.game.online.contract.messages.seats;

import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class AdminId {
    String id;

    public static AdminId of(UserId userId) {
        return new AdminId(userId.getId());
    }

    public static AdminId random() {
        return new AdminId(UUID.randomUUID().toString());
    }
}