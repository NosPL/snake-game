package com.noscompany.snake.game.online.contract.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import com.noscompany.snake.game.online.contract.messages.room.RoomState;
import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import io.vavr.control.Option;
import org.junit.Assert;

import java.util.List;
import java.util.Set;

public class BaseTestClass {

    protected <T extends OnlineMessage> void testSerializationOf(T onlineMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperCreator.createInstance();
        String string = objectMapper.writeValueAsString(onlineMessage);
        Class<T> aClass = (Class<T>) onlineMessage.getClass();
        T serialized = objectMapper.readValue(string, aClass);
        Assert.assertEquals(onlineMessage, serialized);
    }

    protected RoomState roomState() {
        return new RoomState(false, Set.of("a", "b", "c"), lobbyState());
    }

    protected LobbyState lobbyState() {
        return new LobbyState(
                new GameOptions(GridSize._10x10, GameSpeed.x1, Walls.OFF),
                Set.of(new LobbyState.Seat(PlayerNumber._1, Option.of("some name"),  true, true)),
                false,
                gameState());
    }

    protected GameState gameState() {
        return new GameState(List.of(anySnake()), anyGridSize(), anyWalls(), anyFoodPosition(), anyScore());
    }

    private Snake anySnake() {
        return new Snake(PlayerNumber._1, Direction.DOWN, true, List.of(new Snake.Node(Position.position(1, 1), true)));
    }

    private GridSize anyGridSize() {
        return GridSize._5X5;
    }

    private Walls anyWalls() {
        return Walls.OFF;
    }

    private Option<Position> anyFoodPosition() {
        return Option.of(Position.position(2, 2));
    }

    private Score anyScore() {
        return new Score(List.of(new Score.Entry(1, 2, List.of(new Score.Snake(PlayerNumber._1, true)))));
    }
}