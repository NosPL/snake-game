package com.noscompany.snake.game.online.server.room.room.commons;

import org.junit.Before;

public class UserNotInTheRoomSetup extends RoomTestSetup {

    @Before
    public void assertThatUserDidNotEnterTheRoom() {
        assert !room.containsUserWithId(userId);
    }
}