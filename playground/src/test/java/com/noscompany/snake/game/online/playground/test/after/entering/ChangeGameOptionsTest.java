package com.noscompany.snake.game.online.playground.test.after.entering;

import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.seats.AdminId;
import com.noscompany.snake.game.online.playground.test.commons.PlaygroundTestSetup;
import org.junit.Assert;
import org.junit.Test;

public class ChangeGameOptionsTest extends PlaygroundTestSetup {

    @Test
    public void changeGameOptionsHappyPath() {
//        GIVEN that the actor took a seat as admin
        playground.playerTookASeat(actorId, anyPlayerNumber(), AdminId.of(actorId));
//        and game is not running
        assert !playground.gameIsRunning();
//        WHEN he tries to change game options
        var newGameOptions = newGameOptions();
        var result = playground.changeGameOptions(actorId, newGameOptions);
//        THEN he succeeds
        var expected = success(gameOptionsChanged(newGameOptions));
        Assert.assertEquals(expected, result);
    }

    private GameOptionsChanged gameOptionsChanged(GameOptions gameOptions) {
        var currentPlaygroundState = playgroundState();
        var currentGameState = currentPlaygroundState.getGameState();
        var gameRunning = currentPlaygroundState.isGameRunning();
        return new GameOptionsChanged(new PlaygroundState(gameOptions, gameRunning, currentGameState));
    }

    @Test
    public void actorShouldFailToChangeGameOptionsWhenGameIsRunning() {
//        GIVEN that the actor took a seat as admin
        playground.playerTookASeat(actorId, anyPlayerNumber(), AdminId.of(actorId));
//        and the actor started the game
        assert isSuccess(playground.startGame(actorId));
//        WHEN the actor tries to change game options
        var result = playground.changeGameOptions(actorId, newGameOptions());
//        THEN he fails because game is running
        var expected = failure(FailedToChangeGameOptions.gameIsAlreadyRunning(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToChangeGameOptionsIfHeIsNotAnAdmin() {
//        and that the actor took a seat, but he is not admin
        playground.playerTookASeat(actorId, anyPlayerNumber(), AdminId.random());
//        WHEN the actor tries to change game options
        var result = playground.changeGameOptions(actorId, newGameOptions());
//        THEN he fails because he is not the admin
        var expected = failure(FailedToChangeGameOptions.requesterIsNotAdmin(actorId));
        Assert.assertEquals(expected, result);
    }
}