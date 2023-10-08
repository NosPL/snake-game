package com.noscompany.snake.game.online.contract.messages;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.room.*;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import io.vavr.control.Option;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Set;
import java.util.UUID;

import static com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions.Reason.GAME_IS_ALREADY_RUNNING;
import static com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToCancelGame.Reason.USER_NOT_IN_THE_ROOM;

public class SerializationTest extends BaseTestClass {

    @Test
    @SneakyThrows
    public void serialize() {
        var userId = new UserId(UUID.randomUUID().toString());
        testSerializationOf(new SendChatMessage(userId, "some message"));
        testSerializationOf(new CancelGame(userId));
        testSerializationOf(FailedToCancelGame.gameNotStarted(userId));
        testSerializationOf(new ChangeSnakeDirection(userId, Direction.DOWN));
        testSerializationOf(FailedToChangeSnakeDirection.gameNotStarted(userId));
        testSerializationOf(new PauseGame(userId));
        testSerializationOf(FailedToPauseGame.userNotInTheRoom(userId));
        testSerializationOf(new ResumeGame(userId));
        testSerializationOf(FailedToResumeGame.gameNotStarted(userId));
        testSerializationOf(new StartGame(userId));
        testSerializationOf(FailedToStartGame.gameIsAlreadyRunning(userId));
        testSerializationOf(new ChangeGameOptions(userId, GridSize._10x10, GameSpeed.x1, Walls.OFF));
        testSerializationOf(new FreeUpASeat(userId));
        testSerializationOf(new TakeASeat(userId, PlayerNumber._1));
        testSerializationOf(new EnterRoom(userId, "some name"));
        testSerializationOf(new UserSentChatMessage(userId, "some name", "some content"));
        testSerializationOf(FailedToSendChatMessage.userIsNotInTheRoom(userId));
        testSerializationOf(FailedToChangeGameOptions.gameIsAlreadyRunning(userId));
        testSerializationOf(FailedToFreeUpSeat.userDidNotTakeASeat(userId));
        testSerializationOf(FailedToStartGame.gameIsAlreadyRunning(userId));
        testSerializationOf(FailedToTakeASeat.seatAlreadyTaken(userId));
        testSerializationOf(new GameOptionsChanged(lobbyState()));
        testSerializationOf(new PlayerFreedUpASeat(userId, "some name", PlayerNumber._1, lobbyState()));
        testSerializationOf(new PlayerTookASeat(userId, "some name", PlayerNumber._1, lobbyState()));
        testSerializationOf(FailedToEnterRoom.userNameAlreadyInUse(userId));
        testSerializationOf(new NewUserEnteredRoom(userId, "some name", roomState()));
        testSerializationOf(new NewUserEnteredRoom(userId, "some name", roomState()));
        testSerializationOf(new UserLeftRoom(userId, "some name", Set.of("a", "b", "c"), Option.of(playerFreedUpASeat(userId))));
        testSerializationOf(new UserLeftRoom(userId, "some name", Set.of("a", "b", "c"), Option.none()));
    }

    private PlayerFreedUpASeat playerFreedUpASeat(UserId userId) {
        return new PlayerFreedUpASeat(userId, "q", PlayerNumber._1, lobbyState());
    }
}