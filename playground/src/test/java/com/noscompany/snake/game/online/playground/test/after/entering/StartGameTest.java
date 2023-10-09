package com.noscompany.snake.game.online.playground.test.after.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.seats.AdminId;
import com.noscompany.snake.game.online.playground.test.commons.PlaygroundTestSetup;
import io.vavr.control.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StartGameTest extends PlaygroundTestSetup {

    private PlayerNumber freeSeatNumber() {
        return null;
    }

    @Test
    public void actorShouldStartGameAfterTakingASeatInEmptyLobby() {
//        GIVEN that the actor took a seat as admin
        playground.playerTookASeat(actorId, freeSeatNumber(), AdminId.of(actorId));
//        WHEN he tries to start game
        var potentialError = playground.startGame(actorId);
//        THEN no error occurs
        assert potentialError.isEmpty();
    }

    @Test
    public void actorShouldFailToStartGameWhenGameIsAlreadyRunning() {
//        GIVEN that the actor took a seat as admin
        playground.playerTookASeat(actorId, freeSeatNumber(), AdminId.of(actorId));
//        and game is already running
        assert isSuccess(playground.startGame(actorId));
//        WHEN he tries to start game again
        var result = playground.startGame(actorId);
//        THEN he fails because game is already running
        var expected = Option.of(FailedToStartGame.gameIsAlreadyRunning(actorId));
        assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToStartGameWithoutBeingAnAdmin() {
//        WHEN the actor tries to start game
        var result = playground.startGame(actorId);
//        THEN the actor fails because he is not the admin
        var expected = Option.of(FailedToStartGame.requesterIsNotAdmin(actorId));
        assertEquals(expected, result);
    }
}