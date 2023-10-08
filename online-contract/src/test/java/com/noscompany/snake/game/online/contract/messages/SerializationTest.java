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
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.*;
import io.vavr.control.Option;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

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
        testSerializationOf(new NewUserEnteredRoom(userId, "some name", randomUserNames()));
        testSerializationOf(new UserLeftRoom(userId, new UserName("some name"), Set.of(new UserName("a"), new UserName("b"), new UserName("c"))));
        testSerializationOf(new UserLeftRoom(userId, new UserName("some name"), Set.of(new UserName("a"), new UserName("b"), new UserName("c"))));
    }

    private PlayerFreedUpASeat playerFreedUpASeat(UserId userId) {
        return new PlayerFreedUpASeat(userId, "q", PlayerNumber._1, lobbyState());
    }

    private Collection<UserName> randomUserNames() {
        return IntStream.range(0, 5).mapToObj(i -> UserName.random()).toList();
    }
}