package com.noscompany.snake.game.online.host.room.before.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import org.junit.Test;

import static io.vavr.control.Option.of;
import static org.junit.Assert.assertEquals;

public class StartGameTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToStartGame() {
//        WHEN the actor tries to start the game
        var result = room.startGame(randomUserId());
//        THEN he fails due to not being in the room
        var expected = of(FailedToStartGame.userIsNotInTheRoom());
        assertEquals(expected, result);
    }
}