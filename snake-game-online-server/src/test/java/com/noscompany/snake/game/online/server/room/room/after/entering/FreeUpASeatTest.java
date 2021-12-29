package com.noscompany.snake.game.online.server.room.room.after.entering;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToFreeUpSeat;
import com.noscompany.snake.game.commons.messages.events.lobby.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.server.room.room.commons.UserInTheRoomSetup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FreeUpASeatTest extends UserInTheRoomSetup {

    @Test
    public void userShouldFreeUpASeatAfterTakingASeat() {
//        given that the user took a seat
        var number = freeSeatNumber();
        assert room.takeASeat(userId, number).isRight();
//        when he tries to free up a seat
        var result = room.freeUpASeat(userId).get();
//        then he succeeds
        var expected = new PlayerFreedUpASeat(userName, number, lobbyState());
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToFreeUpASeatWithoutTakingASeatFirst() {
//        given that user did not take a seat
        assert !tookASeat(userName);
//        when he tries to free up a seat
        var result = room.freeUpASeat(userId).getLeft();
//        then he fails due to not being in the room
        var expected = FailedToFreeUpSeat.userNotInTheLobby();
        assertEquals(expected, result);
    }
}