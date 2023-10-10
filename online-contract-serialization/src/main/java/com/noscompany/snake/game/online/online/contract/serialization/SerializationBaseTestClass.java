package com.noscompany.snake.game.online.online.contract.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameFinished;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStartCountdown;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStarted;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.SnakesMoved;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.seats.Seat;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import io.vavr.control.Option;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Set;

public class SerializationBaseTestClass {

    @SneakyThrows
    protected <T extends OnlineMessage> void testSerializationOf(T onlineMessage) {
        ObjectMapper objectMapper = ConfiguredObjectMapperCreator.createInstance();
        String string = objectMapper.writeValueAsString(onlineMessage);
        Class<T> aClass = (Class<T>) onlineMessage.getClass();
        T serialized = objectMapper.readValue(string, aClass);
        assert onlineMessage.equals(serialized);
    }

    protected GameStarted gameStarted() {
        return new GameStarted(anyGridSize(), anyWalls(), anyFoodPosition(), List.of(anySnake()), anyScore());
    }

    protected GameFinished gameFinished() {
        return new GameFinished(List.of(anySnake()), anyGridSize(), anyWalls(), anyFoodPosition(), anyScore());
    }

    protected SnakesMoved snakesMoved() {
        return new SnakesMoved(anyGridSize(), anyWalls(), anyFoodPosition(), List.of(anySnake()), anyScore());
    }

    protected GameStartCountdown gameStartCountdown() {
        return new GameStartCountdown(5, List.of(anySnake()), anyGridSize(), anyWalls(), anyFoodPosition(), anyScore());
    }

    protected PlaygroundState playgroundState() {
        return new PlaygroundState(
                new GameOptions(GridSize._10x10, GameSpeed.x1, Walls.OFF),
                false,
                gameState());
    }

    protected GameState gameState() {
        return new GameState(List.of(anySnake()), anyGridSize(), anyWalls(), anyFoodPosition(), anyScore());
    }

    protected Snake anySnake() {
        return new Snake(PlayerNumber._1, Direction.DOWN, true, List.of(new Snake.Node(Position.position(1, 1), true)));
    }

    protected GridSize anyGridSize() {
        return GridSize._5X5;
    }

    protected Walls anyWalls() {
        return Walls.OFF;
    }

    protected Option<Position> anyFoodPosition() {
        return Option.of(Position.position(2, 2));
    }

    protected Score anyScore() {
        return new Score(List.of(new Score.Entry(1, 2, List.of(new Score.Snake(PlayerNumber._1, true)))));
    }

    protected Set<UserName> randomUserNames() {
        return Set.of(UserName.random(), UserName.random(), UserName.random());
    }

    protected Set<Seat> randomSeats() {
        return Set.of(new Seat(PlayerNumber._1, Option.of(UserId.random()), Option.of(UserName.random()), true));
    }
}