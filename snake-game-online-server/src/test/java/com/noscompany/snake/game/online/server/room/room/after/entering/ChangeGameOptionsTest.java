package com.noscompany.snake.game.online.server.room.room.after.entering;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToChangeGameOptions;
import com.noscompany.snake.game.commons.messages.events.lobby.GameOptionsChanged;
import com.noscompany.snake.game.online.server.room.room.commons.UserInTheRoomSetup;
import org.junit.Test;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Walls;

import static org.junit.Assert.assertEquals;

public class ChangeGameOptionsTest extends UserInTheRoomSetup {

    @Test
    public void userShouldChangeGameOptionsAfterTakingASeatAndBeingChosenAsAdmin() {
//        given that the user took a seat
        assert room.takeASeat(userId, freeSeatNumber()).isRight();
//        and he has been chosen as an admin
        assert adminNameEquals(userName);
//        when he tries to change game options
        var result = room.changeGameOptions(userId, gridSize(), gameSpeed(), walls()).get();
//        then he succeeds
        var expected = new GameOptionsChanged(lobbyState());
        assertEquals(expected, result);
        assertEquals(gridSize(), result.getLobbyState().getGridSize());
        assertEquals(gameSpeed(), result.getLobbyState().getGameSpeed());
        assertEquals(walls(), result.getLobbyState().getWalls());
    }

    @Test
    public void userShouldFailToChangeGameOptionsWithoutTakingASeat() {
//        given that the user did not take a seat
        assert !tookASeat(userName);
//        when he tries to change game options
        var result = room.changeGameOptions(userId, gridSize(), gameSpeed(), walls()).getLeft();
//        then he fails because he did not take a seat
        var expected = FailedToChangeGameOptions.requesterDidNotTookASeat();
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToChangeGameOptionsWhenGameIsRunning() {
//        given that the user took a seat
        assert room.takeASeat(userId, freeSeatNumber()).isRight();
//        and game is running
        room.startGame(userId);
        assert gameIsRunning();
//        when the user tries to change game options
        var result = room.changeGameOptions(userId, gridSize(), gameSpeed(), walls()).getLeft();
//        then he fails because game is running
        var expected = FailedToChangeGameOptions.gameIsAlreadyRunning();
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToChangeGameOptionsWithoutBeingAnAdmin() {
//        given that admin is already chosen
        takeAnySeatWithRandomUser();
        assert adminIsChosen();
//        when the user takes a seat
        assert room.takeASeat(userId, freeSeatNumber()).isRight();
//        and he tries to change game options
        var result = room.changeGameOptions(userId, gridSize(), gameSpeed(), walls()).getLeft();
//        then he fails because he is not the admin
        var expected = FailedToChangeGameOptions.requesterIsNotAdmin();
        assertEquals(expected, result);
    }

    private GridSize gridSize() {
        return GridSize._10x10;
    }

    private GameSpeed gameSpeed() {
        return GameSpeed.x1;
    }

    private Walls walls() {
        return Walls.OFF;
    }
}