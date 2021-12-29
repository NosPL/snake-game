package com.noscompany.snake.game.online.server.room.room.after.entering;

import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.server.room.room.commons.UserInTheRoomSetup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnterTheRoomAgainTest extends UserInTheRoomSetup {

    @Test
    public void userShouldFailToEnterTheRoomForTheSecondTimeWithADifferentName() {
//        WHEN the user tries to enter the room again with a different name
        var result = room.enter(userId, randomValidUserName()).getLeft();
//        THEN he fails because he entered the room already
        var expected = FailedToEnterRoom.userAlreadyInTheRoom(userName);
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToEnterTheRoomForTheSecondTimeWithTheSameName() {
//        WHEN the user tries to enter the room again with the same name
        var result = room.enter(userId, userName).getLeft();
//        THEN he fails due to being in the room already
        var expected = FailedToEnterRoom.userAlreadyInTheRoom(userName);
        assertEquals(expected, result);
    }
}