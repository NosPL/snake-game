package com.noscompany.snake.game.commons.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.commons.OnlineMessage;
import com.noscompany.snake.game.commons.messages.dto.LobbyAdmin;
import com.noscompany.snake.game.commons.messages.dto.LobbyState;
import com.noscompany.snake.game.commons.object.mapper.ObjectMapperCreator;
import io.vavr.control.Option;
import org.junit.Assert;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.dto.*;

import java.util.Map;

public class BaseTestClass {

    protected <T extends OnlineMessage> void testSerializationOf(T onlineMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperCreator.createInstance();
        String string = objectMapper.writeValueAsString(onlineMessage);
        Class<T> aClass = (Class<T>) onlineMessage.getClass();
        T serialized = objectMapper.readValue(string, aClass);
        Assert.assertEquals(onlineMessage, serialized);
    }

    protected LobbyState lobbyState() {
        return new LobbyState(
                GridSize._10x10,
                GameSpeed.x1,
                Walls.OFF,
                Option.of(new LobbyAdmin("some name", SnakeNumber._1)),
                Map.of("some name", SnakeNumber._1),
                false,
                gameState());
    }

    protected GameState gameState() {
        return new SnakeGameConfiguration()
                .set(SnakeNumber._1)
                .create()
                .get()
                .getGameState();
    }
}