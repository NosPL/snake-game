package com.noscompany.snake.game.commons.messages;

import com.noscompany.snake.game.commons.messages.commands.chat.SendChatMessage;
import com.noscompany.snake.game.commons.messages.commands.game.*;
import com.noscompany.snake.game.commons.messages.commands.lobby.ChangeGameOptions;
import com.noscompany.snake.game.commons.messages.commands.lobby.FreeUpASeat;
import com.noscompany.snake.game.commons.messages.commands.lobby.TakeASeat;
import com.noscompany.snake.game.commons.messages.commands.room.EnterRoom;
import com.noscompany.snake.game.commons.messages.events.chat.UserSentChatMessage;
import com.noscompany.snake.game.commons.messages.events.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.commons.messages.events.lobby.*;
import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import com.noscompany.snake.game.commons.messages.events.room.NewUserEnteredRoom;
import com.noscompany.snake.game.commons.messages.events.room.UserLeftRoom;
import io.vavr.control.Option;
import lombok.SneakyThrows;
import org.junit.Test;
import snake.game.core.dto.*;

import java.util.Set;

public class SerializationTest extends BaseTestClass {

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
        testSerializationOf(new UserSentChatMessage("some name", "some content"));
        testSerializationOf(FailedToSendChatMessage.userIsNotInTheRoom());
        testSerializationOf(FailedToChangeGameOptions.gameIsAlreadyRunning());
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

}