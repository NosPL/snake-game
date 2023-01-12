package com.noscompany.snake.game.online.contract.messages;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.commands.*;
import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import com.noscompany.snake.game.online.contract.messages.lobby.command.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.lobby.command.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.lobby.command.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.lobby.event.*;
import com.noscompany.snake.game.online.contract.messages.room.*;
import io.vavr.control.Option;
import lombok.SneakyThrows;
import org.junit.Test;

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
        testSerializationOf(new TakeASeat(PlayerNumber._1));
        testSerializationOf(new EnterRoom("some name"));
        testSerializationOf(new UserSentChatMessage("some name", "some content"));
        testSerializationOf(FailedToSendChatMessage.userIsNotInTheRoom());
        testSerializationOf(FailedToChangeGameOptions.gameIsAlreadyRunning());
        testSerializationOf(FailedToFreeUpSeat.userDidNotTakeASeat());
        testSerializationOf(FailedToStartGame.gameIsAlreadyRunning());
        testSerializationOf(FailedToTakeASeat.seatAlreadyTaken());
        testSerializationOf(new GameOptionsChanged(lobbyState()));
        testSerializationOf(new PlayerFreedUpASeat("some name", PlayerNumber._1, lobbyState()));
        testSerializationOf(new PlayerTookASeat("some name", PlayerNumber._1, lobbyState()));
        testSerializationOf(FailedToEnterRoom.userNameAlreadyInUse("some name"));
        testSerializationOf(new NewUserEnteredRoom("some name", roomState()));
        testSerializationOf(new NewUserEnteredRoom("some name", roomState()));
        testSerializationOf(new UserLeftRoom("some name", Set.of("a", "b", "c"), Option.of(playerFreedUpASeat())));
        testSerializationOf(new UserLeftRoom("some name", Set.of("a", "b", "c"), Option.none()));
    }

    private PlayerFreedUpASeat playerFreedUpASeat() {
        return new PlayerFreedUpASeat("q", PlayerNumber._1, lobbyState());
    }
}