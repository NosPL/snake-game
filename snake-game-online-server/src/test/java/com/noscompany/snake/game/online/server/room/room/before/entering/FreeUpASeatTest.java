package com.noscompany.snake.game.online.server.room.room.before.entering;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.server.room.room.commons.UserNotInTheRoomSetup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FreeUpASeatTest extends UserNotInTheRoomSetup {

    @Test
    public void userShouldFailToFreeUpASeat() {
//        WHEN the user tries to free up a seat
        var result = room.freeUpASeat(randomUserId()).getLeft();
//        THEN he fails due to not being in the room
        var expected = FailedToFreeUpSeat.userNotInTheRoom();
        assertEquals(expected, result);
    }
}