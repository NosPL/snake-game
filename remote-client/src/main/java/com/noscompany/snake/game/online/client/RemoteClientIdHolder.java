package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.network.YourIdGotInitialized;
import io.vavr.control.Option;

public final class RemoteClientIdHolder {
    private static Option<UserId> userIdOption = Option.none();

    public static void reset() {
        userIdOption = Option.none();
    }

    public static void yourIdGotInitialized(YourIdGotInitialized event) {
        userIdOption = Option.of(event.getUserId());
    }

    public static Option<UserId> userId() {
        return userIdOption;
    }
}