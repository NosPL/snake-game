package com.noscompany.snake.game.online.gameplay.supervisor;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import io.vavr.control.Option;
import snake.game.gameplay.GameplayCreator;

import java.util.HashMap;
import java.util.Set;

public final class GameplaySupervisorConfiguration {

    public GameplaySupervisor createPlayground(MessagePublisher messagePublisher, GameplayCreator gameplayCreator, GameOptions defaultGameOptions) {
        var gameplayEventHandler = new GamePlayEventsMessagePublisherAdapter(messagePublisher);
        var gameCreatorAdapter = new GameCreatorAdapter(gameplayCreator, gameplayEventHandler);
        var snakeGame = gameCreatorAdapter.createGame(Set.of(), defaultGameOptions);
        var playground = new GameplaySupervisor(new HashMap<>(), Option.none(), gameCreatorAdapter, defaultGameOptions, snakeGame);
        var subscription = playground.getSubscription();
        messagePublisher.subscribe(subscription);
        return playground;
    }
}