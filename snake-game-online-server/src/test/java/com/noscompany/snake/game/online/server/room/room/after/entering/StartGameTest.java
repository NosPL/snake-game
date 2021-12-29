package com.noscompany.snake.game.online.server.room.room.after.entering;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToStartGame;
import com.noscompany.snake.game.online.server.room.room.commons.UserInTheRoomSetup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StartGameTest extends UserInTheRoomSetup {

    @Test
    public void userShouldStartGameAfterTakingASeatInEmptyLobby() {
//        given that the user is as an admin
        room.takeASeat(userId, freeSeatNumber());
        assert adminNameEquals(userName);
//        when he tries to start game
        var potentialError = room.startGame(userId);
//        then no error occurs
        assert potentialError.isEmpty();
    }

    @Test
    public void userShouldFailToStartGameWithoutTakingASeatInTheLobby() {
//        given that the user did not take a seat in the lobby
        assert !tookASeat(userName);
//        when he tries to start game
        var result = room.startGame(userId).get();
//        then he fails due to not being in the room
        var expected = FailedToStartGame.requesterDidNotTookASeat(lobbyState());
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToStartGameWhenGameIsRunningAlready() {
//        given that the user is an admin
        room.takeASeat(userId, freeSeatNumber());
        assert adminNameEquals(userName);
//        and game is already running
        room.startGame(userId);
        assert gameIsRunning();
//        when he tries to start game again
        var result = room.startGame(userId).get();
//        then he fails because game is already running
        var expected = FailedToStartGame.gameIsAlreadyRunning(lobbyState());
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToStartGameWithoutBeingAnAdmin() {
//        given that an admin is chosen
        takeAnySeatWithRandomUser();
        assert adminIsChosen();
//        and the user took a seat
        assert room.takeASeat(userId, freeSeatNumber()).isRight();
//        and the user is not an admin
        assert !adminNameEquals(userName);
//        when the user tries to start game
        var result = room.startGame(userId).get();
//        then the user fails because he is not the admin
        var expected = FailedToStartGame.requesterIsNotAdmin(lobbyState());
        assertEquals(expected, result);
    }
}