package com.noscompany.snake.game.online.playground.test.after.entering;

import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import org.junit.Assert;
import org.junit.Test;

public class ChangeGameOptionsTest extends ActorEnteredTheRoomSetup {

    @Test
    public void actorShouldSuccessfullyChangeGameOptionsIfHeTookASeatAndHeIsAdminAndGameIsNotRunning() {
//        GIVEN that the actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        and he is an admin
        assert playground.userIsAdmin(actorId);
//        and game is not running
        assert !gameIsRunning();
//        WHEN he tries to change game options
        var result = playground.changeGameOptions(actorId, newGameOptions());
//        THEN he succeeds
        var expected = success(gameOptionsChanged());
        Assert.assertEquals(expected, result);
    }

    private GameOptionsChanged gameOptionsChanged() {
        return new GameOptionsChanged(lobbyState());
    }

    @Test
    public void actorShouldFailToChangeGameOptionsWithoutTakingASeat() {
//        GIVEN that the actor did not take a seat
        assert !playground.userIsSitting(actorId);
//        WHEN he tries to change game options
        var result = playground.changeGameOptions(actorId, newGameOptions());
//        THEN he fails because he did not take a seat
        var expected = failure(FailedToChangeGameOptions.requesterDidNotTakeASeat(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToChangeGameOptionsWhenGameIsRunning() {
//        GIVEN that the actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        and the actor is the admin
        assert playground.userIsAdmin(actorId);
//        and the actor started the game
        playground.startGame(actorId);
        assert gameIsRunning();
//        WHEN the actor tries to change game options
        var result = playground.changeGameOptions(actorId, newGameOptions());
//        THEN he fails because game is running
        var expected = failure(FailedToChangeGameOptions.gameIsAlreadyRunning(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToChangeGameOptionsIfHeIsNotAnAdmin() {
//        GIVEN that the admin has been already chosen
        someRandomUserTakesASeat();
        assert adminIsChosen();
//        and that the actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        WHEN the actor tries to change game options
        var result = playground.changeGameOptions(actorId, newGameOptions());
//        THEN he fails because he is not the admin
        var expected = failure(FailedToChangeGameOptions.requesterIsNotAdmin(actorId));
        Assert.assertEquals(expected, result);
    }
}