package com.noscompany.snake.game.online.game.options.setter.test;

import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.seats.AdminId;
import org.junit.Assert;
import org.junit.Test;

public class ChangeGameOptionsTest extends TestSetup {

    @Test
    public void changeGameOptionsHappyPath() {
//        GIVEN that the actor became admin
        gameOptionsSetter.adminGotSet(AdminId.of(actorId));
//        and game is not running
        assert !gameOptionsSetter.isGameRunning();
//        WHEN he tries to change game options
        var newGameOptions = newGameOptions();
        var result = gameOptionsSetter.changeGameOptions(actorId, newGameOptions);
//        THEN he succeeds
        var expected = success(new GameOptionsChanged(newGameOptions));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToChangeGameOptionsWhenGameIsRunning() {
//        GIVEN that the actor became admin
        gameOptionsSetter.adminGotSet(AdminId.of(actorId));
//        and the actor started the game
        gameOptionsSetter.gameIsNowRunning();
//        WHEN the actor tries to change game options
        var result = gameOptionsSetter.changeGameOptions(actorId, newGameOptions());
//        THEN he fails because game is running
        var expected = failure(FailedToChangeGameOptions.gameIsAlreadyRunning(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToChangeGameOptionsIfHeIsNotAnAdmin() {
//        and that the actor is not admin
        gameOptionsSetter.adminGotSet(AdminId.random());
//        WHEN the actor tries to change game options
        var result = gameOptionsSetter.changeGameOptions(actorId, newGameOptions());
//        THEN he fails because he is not the admin
        var expected = failure(FailedToChangeGameOptions.requesterIsNotAdmin(actorId));
        Assert.assertEquals(expected, result);
    }
}