package com.noscompany.snake.game.online.host.room.room.after.entering;

import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToStartGame;
import io.vavr.control.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StartGameTest extends ActorEnteredTheRoomSetup {

    @Test
    public void actorShouldStartGameAfterTakingASeatInEmptyLobby() {
//        GIVEN that the actor took a seat
        room.takeASeat(actorId, freeSeatNumber());
//        and the actor is the admin
        assert room.userIsAdmin(actorId);
//        WHEN he tries to start game
        var potentialError = room.startGame(actorId);
//        THEN no error occurs
        assert potentialError.isEmpty();
    }

    @Test
    public void actorShouldFailToStartGameWithoutTakingASeatInTheLobby() {
//        GIVEN that the actor did not take a seat
        assert !room.userIsSitting(actorId);
//        WHEN he tries to start game
        var result = room.startGame(actorId).get();
//        THEN he fails due to not being in the room
        var expected = FailedToStartGame.requesterDidNotTakeASeat();
        assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToStartGameWhenGameIsAlreadyRunning() {
//        GIVEN that the actor took a seat
        room.takeASeat(actorId, freeSeatNumber());
//        and the actor is the admin
        assert room.userIsAdmin(actorId);
//        and game is already running
        room.startGame(actorId);
        assert gameIsRunning();
//        WHEN he tries to start game again
        var result = room.startGame(actorId);
//        THEN he fails because game is already running
        var expected = Option.of(FailedToStartGame.gameIsAlreadyRunning());
        assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToStartGameWithoutBeingAnAdmin() {
//        GIVEN that the admin is already chosen
        someRandomUserTakesASeat();
        assert adminIsChosen();
//        and the actor took a seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        WHEN the actor tries to start game
        var result = room.startGame(actorId);
//        THEN the actor fails because he is not the admin
        var expected = Option.of(FailedToStartGame.requesterIsNotAdmin());
        assertEquals(expected, result);
    }
}