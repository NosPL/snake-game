package com.noscompany.snake.game.online.server.room.room.before.entering;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToStartGame;
import com.noscompany.snake.game.online.server.room.room.commons.UserNotInTheRoomSetup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StartGameTest extends UserNotInTheRoomSetup {

    @Test
    public void userShouldFailToStartGame() {
//        WHEN the user tries to start the game
        var result = room.startGame(randomUserId()).get();
//        THEN he fails due to not being in the room
        var expected = FailedToStartGame.userIsNotInTheRoom(getLobbyState());
        assertEquals(expected, result);
    }
}