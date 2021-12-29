package com.noscompany.snake.game.online.server.room.room.before.entering;

import com.noscompany.snake.game.online.server.room.room.commons.UserNotInTheRoomSetup;
import org.junit.Test;

public class RemoveUserTest extends UserNotInTheRoomSetup {

    @Test
    public void removingUserThatDidNotEnterTheRoomShouldNotProduceAnyEvent() {
//        WHEN someone tries to remove the user from the room using his id
        var result = room.removeUserById(userId);
//        THEN nothing happens
        assert result.isEmpty();
    }
}