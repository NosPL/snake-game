package com.noscompany.snake.game.onlin.online.contract.serialization.test;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.game.options.InitializeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.network.YourIdGotInitialized;
import com.noscompany.snake.game.online.contract.messages.playground.GameReinitialized;
import com.noscompany.snake.game.online.contract.messages.playground.InitializeGame;
import com.noscompany.snake.game.online.contract.messages.seats.*;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.user.registry.*;
import io.vavr.control.Option;
import lombok.SneakyThrows;
import org.junit.Test;

public class SerializationTest extends SerializationBaseTestClass {

    @Test
    @SneakyThrows
    public void serialize() {
//        server events
        testSerializationOf(new ServerGotShutdown());

//        remote client id initialization
        testSerializationOf(new YourIdGotInitialized(UserId.random()));

//        user registry messages
        testSerializationOf(new EnterRoom(UserId.random(), UserName.random()));
        testSerializationOf(new NewUserEnteredRoom(UserId.random(), UserName.random(), randomUserNames()));
        testSerializationOf(FailedToEnterRoom.userNameAlreadyInUse(UserId.random()));
        testSerializationOf(new UserLeftRoom(UserId.random(), UserName.random(), randomUserNames()));

//        gameplay supervisor messages
        testSerializationOf(new InitializeGame(UserId.random(), playgroundState()));
        testSerializationOf(new GameReinitialized(gameState()));

//        game options
        testSerializationOf(new InitializeGameOptions(UserId.random(), playgroundState().getGameOptions()));
        testSerializationOf(new ChangeGameOptions(UserId.random(), GridSize._10x10, GameSpeed.x1, Walls.OFF));
        testSerializationOf(new GameOptionsChanged(playgroundState().getGameOptions()));
        testSerializationOf(FailedToChangeGameOptions.gameIsAlreadyRunning(UserId.random()));

//        seats messages
        testSerializationOf(new InitializeSeats(UserId.random(), Option.of(AdminId.random()), randomSeats()));
        testSerializationOf(new TakeASeat(UserId.random(), PlayerNumber._1));
        testSerializationOf(new PlayerTookASeat(UserId.random(), UserName.random(), PlayerNumber._1, AdminId.random(),  randomSeats()));
        testSerializationOf(FailedToTakeASeat.seatAlreadyTaken(UserId.random()));
        testSerializationOf(new FreeUpASeat(UserId.random()));
        testSerializationOf(new PlayerFreedUpASeat(UserId.random(), PlayerNumber._1, Option.of(AdminId.random()), randomSeats()));
        testSerializationOf(FailedToFreeUpSeat.userDidNotTakeASeat(UserId.random()));

        //        gameplay messages
        testSerializationOf(new StartGame(UserId.random()));
        testSerializationOf(new ChangeSnakeDirection(UserId.random(), Direction.DOWN));
        testSerializationOf(new CancelGame(UserId.random()));
        testSerializationOf(new PauseGame(UserId.random()));
        testSerializationOf(new ResumeGame(UserId.random()));
        testSerializationOf(gameStartCountdown());
        testSerializationOf(gameStarted());
        testSerializationOf(snakesMoved());
        testSerializationOf(gameFinished());
        testSerializationOf(new GamePaused());
        testSerializationOf(new GameResumed());
        testSerializationOf(new GameCancelled());
        testSerializationOf(FailedToStartGame.gameIsAlreadyRunning(UserId.random()));
        testSerializationOf(FailedToCancelGame.gameNotStarted(UserId.random()));
        testSerializationOf(FailedToChangeSnakeDirection.gameNotStarted(UserId.random()));
        testSerializationOf(FailedToPauseGame.userNotInTheRoom(UserId.random()));
        testSerializationOf(FailedToResumeGame.gameNotStarted(UserId.random()));
    }
}