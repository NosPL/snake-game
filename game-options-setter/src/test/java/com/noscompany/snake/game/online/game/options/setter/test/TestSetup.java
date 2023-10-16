package com.noscompany.snake.game.online.game.options.setter.test;

import com.noscompany.message.publisher.utils.NullMessagePublisher;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameSpeed;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.game.options.setter.GameOptionsSetter;
import com.noscompany.snake.game.online.game.options.setter.GameOptionsSetterConfiguration;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.Before;

import java.util.stream.Stream;

public class TestSetup {
    protected int userIdCounter;
    protected int userNameCounter;
    protected GameOptionsSetter gameOptionsSetter;
    protected UserId actorId;
    protected UserName actorName;

    @Before
    public void init() {
        userIdCounter = 0;
        userNameCounter = 0;
        gameOptionsSetter = new GameOptionsSetterConfiguration()
                .create(new NullMessagePublisher(), new GameOptions(GridSize._10x10, GameSpeed.x1, Walls.ON));
        actorId = UserId.random();
        actorName = UserName.random();
    }

    protected <L, R> Either<L, R> failure(L failure) {
        return Either.left(failure);
    }

    protected <L, R> Either<L, R> success(R success) {
        return Either.right(success);
    }

    protected GameOptions currentGameOptions() {
        return gameOptionsSetter.getGameOptions();
    }

    protected GameOptions newGameOptions() {
        return new GameOptions(newGridSize(), newGameSpeed(), newWalls());
    }

    private GridSize newGridSize() {
        return Stream.of(GridSize.values())
                .filter(gridSize -> !gridSize.equals(currentGameOptions().getGridSize()))
                .findAny()
                .get();
    }

    private GameSpeed newGameSpeed() {
        return Stream.of(GameSpeed.values())
                .filter(gameSpeed -> !gameSpeed.equals(currentGameOptions().getGameSpeed()))
                .findAny()
                .get();
    }

    private Walls newWalls() {
        return Stream.of(Walls.values())
                .filter(walls -> !walls.equals(currentGameOptions().getWalls()))
                .findAny()
                .get();
    }
}