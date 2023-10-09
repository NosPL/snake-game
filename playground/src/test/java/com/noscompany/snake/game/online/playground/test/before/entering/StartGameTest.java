package com.noscompany.snake.game.online.playground.test.before.entering;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import org.junit.Test;

import static io.vavr.control.Option.of;
import static org.junit.Assert.assertEquals;

public class StartGameTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToStartGame() {
//        WHEN the actor tries to start the game
        UserId randomUserId = randomUserId();
        var result = playground.startGame(randomUserId);
//        THEN he fails due to not being in the room
        var expected = of(FailedToStartGame.userIsNotInTheRoom(randomUserId));
        assertEquals(expected, result);
    }
}