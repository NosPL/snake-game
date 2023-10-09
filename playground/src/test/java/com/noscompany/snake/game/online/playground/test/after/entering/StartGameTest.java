package com.noscompany.snake.game.online.playground.test.after.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import io.vavr.control.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StartGameTest extends ActorEnteredTheRoomSetup {

    @Test
    public void actorShouldStartGameAfterTakingASeatInEmptyLobby() {
//        GIVEN that the actor took a seat
        playground.takeASeat(actorId, freeSeatNumber());
//        and the actor is the admin
        assert playground.userIsAdmin(actorId);
//        WHEN he tries to start game
        var potentialError = playground.startGame(actorId);
//        THEN no error occurs
        assert potentialError.isEmpty();
    }

    @Test
    public void actorShouldFailToStartGameWithoutTakingASeatInTheLobby() {
//        GIVEN that the actor did not take a seat
        assert !playground.userIsSitting(actorId);
//        WHEN he tries to start game
        var result = playground.startGame(actorId).get();
//        THEN he fails due to not being in the room
        var expected = FailedToStartGame.requesterDidNotTakeASeat(actorId);
        assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToStartGameWhenGameIsAlreadyRunning() {
//        GIVEN that the actor took a seat
        playground.takeASeat(actorId, freeSeatNumber());
//        and the actor is the admin
        assert playground.userIsAdmin(actorId);
//        and game is already running
        playground.startGame(actorId);
        assert gameIsRunning();
//        WHEN he tries to start game again
        var result = playground.startGame(actorId);
//        THEN he fails because game is already running
        var expected = Option.of(FailedToStartGame.gameIsAlreadyRunning(actorId));
        assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToStartGameWithoutBeingAnAdmin() {
//        GIVEN that the admin is already chosen
        someRandomUserTakesASeat();
        assert adminIsChosen();
//        and the actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        WHEN the actor tries to start game
        var result = playground.startGame(actorId);
//        THEN the actor fails because he is not the admin
        var expected = Option.of(FailedToStartGame.requesterIsNotAdmin(actorId));
        assertEquals(expected, result);
    }
}