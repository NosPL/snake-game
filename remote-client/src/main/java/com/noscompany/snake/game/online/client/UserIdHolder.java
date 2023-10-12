package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.online.contract.messages.UserId;
import io.vavr.control.Option;

public final class UserIdHolder {
    private static Option<UserId> userIdOption = Option.none();

    public static void reset() {
        userIdOption = Option.none();
    }

    public static void set(UserId userId) {
        userIdOption = Option.of(userId);
    }

    public static Option<UserId> userId() {
        return userIdOption;
    }
}