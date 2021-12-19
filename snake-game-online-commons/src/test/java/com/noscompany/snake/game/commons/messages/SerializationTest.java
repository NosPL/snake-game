package com.noscompany.snake.game.commons.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.commons.OnlineMessage;
import com.noscompany.snake.game.commons.messages.commands.chat.SendChatMessage;
import com.noscompany.snake.game.commons.messages.commands.game.*;
import com.noscompany.snake.game.commons.messages.commands.lobby.ChangeGameOptions;
import com.noscompany.snake.game.commons.messages.commands.lobby.FreeUpASeat;
import com.noscompany.snake.game.commons.messages.commands.lobby.TakeASeat;
import com.noscompany.snake.game.commons.messages.commands.room.EnterRoom;
import com.noscompany.snake.game.commons.messages.dto.GameLobbyState;
import com.noscompany.snake.game.commons.messages.dto.LobbyAdmin;
import com.noscompany.snake.game.commons.messages.events.chat.ChatMessageReceived;
import com.noscompany.snake.game.commons.messages.events.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.commons.messages.events.lobby.*;
import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import com.noscompany.snake.game.commons.messages.events.room.NewUserEnteredRoom;
import com.noscompany.snake.game.commons.messages.events.room.UserLeftRoom;
import com.noscompany.snake.game.commons.object.mapper.ObjectMapperCreator;
import io.vavr.control.Option;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.dto.*;

import java.util.Map;
import java.util.Set;

public class SerializationTest {

    @Test
    @SneakyThrows
    public void serialize() {
        testSerializationOf(new SendChatMessage("some message"));
        testSerializationOf(new CancelGame());
        testSerializationOf(new ChangeSnakeDirection(Direction.DOWN));
        testSerializationOf(new PauseGame());
        testSerializationOf(new ResumeGame());
        testSerializationOf(new StartGame());
        testSerializationOf(new ChangeGameOptions(GridSize._10x10, GameSpeed.x1, Walls.OFF));
        testSerializationOf(new FreeUpASeat());
        testSerializationOf(new TakeASeat(SnakeNumber._1));
        testSerializationOf(new EnterRoom("some name"));
        testSerializationOf(new ChatMessageReceived("some name", "some content"));
        testSerializationOf(FailedToSendChatMessage.userIsNotInTheRoom());
        testSerializationOf(FailedToChangeGameOptions.gameIsAlreadyRunning(lobbyState()));
        testSerializationOf(FailedToFreeUpSeat.userNotInTheLobby());
        testSerializationOf(FailedToStartGame.gameIsAlreadyRunning(lobbyState()));
        testSerializationOf(FailedToTakeASeat.seatAlreadyTaken(lobbyState()));
        testSerializationOf(new GameOptionsChanged(lobbyState()));
        testSerializationOf(new PlayerFreedUpASeat("some name", SnakeNumber._1, lobbyState()));
        testSerializationOf(new PlayerTookASeat("some name", SnakeNumber._1, lobbyState()));
        testSerializationOf(FailedToEnterRoom.userNameAlreadyInUse("some name"));
        testSerializationOf(new NewUserEnteredRoom("some name", Set.of("a", "b", "c")));
        testSerializationOf(new NewUserEnteredRoom("some name", Set.of("a", "b", "c")));
        testSerializationOf(new UserLeftRoom(
                "some name",
                Set.of("a", "b", "c"),
                Option.of(new PlayerFreedUpASeat("q", SnakeNumber._1, lobbyState()))));
        testSerializationOf(new UserLeftRoom("some name", Set.of("a", "b", "c"), Option.none()));
    }

    private <T extends OnlineMessage>void testSerializationOf(T onlineMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperCreator.createInstance();
        String string = objectMapper.writeValueAsString(onlineMessage);
        Class<T> aClass = (Class<T>) onlineMessage.getClass();
        T serialized = objectMapper.readValue(string, aClass);
        Assert.assertEquals(onlineMessage, serialized);
    }

    private GameLobbyState lobbyState() {
        return new GameLobbyState(
                GridSize._10x10,
                GameSpeed.x1,
                Walls.OFF,
                Option.of(new LobbyAdmin("some name", SnakeNumber._1)),
                Map.of("some name", SnakeNumber._1),
                false,
                gameState());
    }

    private GameState gameState() {
        return new SnakeGameConfiguration()
                .set(SnakeNumber._1)
                .create()
                .get()
                .getGameState();
    }
}