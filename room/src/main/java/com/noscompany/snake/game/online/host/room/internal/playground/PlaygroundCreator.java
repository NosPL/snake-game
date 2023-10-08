package com.noscompany.snake.game.online.host.room.internal.playground;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameSpeed;
import snake.game.gameplay.Gameplay;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.ports.GameplayEventHandler;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;

import java.util.Set;

public class PlaygroundCreator {

    public static Playground create(MessagePublisher messagePublisher,
                                    GameplayCreator gameplayCreator) {
        var gameplayEventHandler = new GamePlayEventsMessagePublisherAdapter(messagePublisher);
        var gameCreatorAdapter = new GameCreatorAdapter(gameplayCreator, gameplayEventHandler);
        var gameOptions = new GameOptions(GridSize._10x10, GameSpeed.x1, Walls.ON);
        var snakeGame = gameCreatorAdapter.createGame(Set.of(), gameOptions);
        return new Playground(SeatsCreator.create(), gameCreatorAdapter, gameOptions, snakeGame);
    }
}