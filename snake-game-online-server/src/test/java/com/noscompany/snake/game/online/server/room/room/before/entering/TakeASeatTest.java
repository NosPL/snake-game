package com.noscompany.snake.game.online.server.room.room.before.entering;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToTakeASeat;
import com.noscompany.snake.game.online.server.room.room.commons.UserNotInTheRoomSetup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TakeASeatTest extends UserNotInTheRoomSetup {

    @Test
    public void userShouldFailToTakeAFreeSeat() {
//        WHEN the user tries to take a seat
        var result = room.takeASeat(userId, freeSeatNumber()).getLeft();
//        THEN he fails due to not being in the room
        var expected = FailedToTakeASeat.userNotInTheRoom(getLobbyState());
        assertEquals(expected, result);
    }
}