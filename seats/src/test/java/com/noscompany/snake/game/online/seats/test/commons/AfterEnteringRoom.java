package com.noscompany.snake.game.online.seats.test.commons;

import org.junit.Before;

public class AfterEnteringRoom extends SeatsTestSetup {

    @Before
    public void AfterEnteringRoomInit() {
        seats.newUserEnteredRoom(actorId, actorName);
    }
}