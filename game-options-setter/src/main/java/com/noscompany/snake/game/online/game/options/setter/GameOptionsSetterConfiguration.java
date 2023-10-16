package com.noscompany.snake.game.online.game.options.setter;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import io.vavr.control.Option;

public final class GameOptionsSetterConfiguration {

    public GameOptionsSetter create(MessagePublisher messagePublisher, GameOptions gameOptions) {
        var gameOptionsSetter = new GameOptionsSetter(Option.none(), false, gameOptions);
        var subscription = gameOptionsSetter.getSubscription();
        messagePublisher.subscribe(subscription);
        return gameOptionsSetter;
    }
}