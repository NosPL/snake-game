package com.noscompany.snake.game.online.host.room.after.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToCancelGame;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.host.room.before.entering.ActorNotInTheRoomSetup;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class CancellingTest extends ActorEnteredTheRoomSetup {

    @Test
    public void cancellingGameShouldNotReturnFailure() {
//        GIVEN that actor took a seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        AND actor is the admin
        assert room.userIsAdmin(actorId);
//        AND game is started
        isSuccess(room.startGame(actorId));
//        WHEN the actor tries to cancel game
        var result = room.cancelGame(actorId);
//        THEN he succeeds
        var expected = Option.none();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToCancelGameWithoutTakingASeat() {
//        WHEN the actor tries to cancel game
        var result = room.cancelGame(actorId);
//        THEN he fails due to not taking a seat
        var expected = Option.of(FailedToCancelGame.playerDidNotTakeASeat());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToCancelGameWithoutBeingAdmin() {
//        GIVEN that someone else is an admin
        someRandomUserTakesASeat();
        adminIsChosen();
//        AND actor took a seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        WHEN the actor tries to cancel game
        var result = room.cancelGame(actorId);
//        THEN he fails due to not being an admin
        var expected = Option.of(FailedToCancelGame.playerIsNotAdmin());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToCancelGameWhenGameIsNotStarted() {
//        GIVEN that actor took a seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        AND actor is the admin
        assert room.userIsAdmin(actorId);
//        WHEN actor tries to cancel game
        var result = room.cancelGame(actorId);
//        THEN he fails because game is not running
        var expected = Option.of(FailedToCancelGame.gameNotStarted());
        Assert.assertEquals(expected, result);
    }
}