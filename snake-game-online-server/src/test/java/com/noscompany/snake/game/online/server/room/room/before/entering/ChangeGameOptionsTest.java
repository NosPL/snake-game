package com.noscompany.snake.game.online.server.room.room.before.entering;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.server.room.room.commons.UserNotInTheRoomSetup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static snake.game.core.dto.GameSpeed.x1;
import static snake.game.core.dto.GridSize._10x10;
import static snake.game.core.dto.Walls.OFF;

public class ChangeGameOptionsTest extends UserNotInTheRoomSetup {

    @Test
    public void userShouldFailToChangeGameOptions() {
//        WHEN the user tries to change game options
        var result = room.changeGameOptions(randomUserId(), _10x10, x1, OFF).getLeft();
//        THEN he fails due to not being in the room
        var expected = FailedToChangeGameOptions.userNotInTheRoom();
        assertEquals(expected, result);
    }
}