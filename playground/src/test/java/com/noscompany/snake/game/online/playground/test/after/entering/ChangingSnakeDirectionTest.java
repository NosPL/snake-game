package com.noscompany.snake.game.online.playground.test.after.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToChangeSnakeDirection;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class ChangingSnakeDirectionTest extends ActorEnteredTheRoomSetup {

    @Test
    public void changingSnakeDirectionShouldNotReturnFailure() {
//        GIVEN that actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        AND game is started
        isSuccess(playground.startGame(actorId));
//        WHEN the actor tries to change snake direction
        var result = playground.changeSnakeDirection(actorId, anyDirection());
//        THEN he succeeds
        var expected = Option.none();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToChangeDirectionWithoutTakingASeat() {
//        WHEN the actor tries to change direction
        var result = playground.changeSnakeDirection(actorId, anyDirection());
//        THEN he fails due to not taking a seat
        var expected = Option.of(FailedToChangeSnakeDirection.playerDidNotTakeASeat(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToChangeSnakeDirectionWhenGameIsNotStarted() {
//        GIVEN that actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        WHEN the actor tries to change snake direction
        var result = playground.changeSnakeDirection(actorId, anyDirection());
//        THEN he fails because game is not running
        var expected = Option.of(FailedToChangeSnakeDirection.gameNotStarted(actorId));
        Assert.assertEquals(expected, result);
    }

    private Direction anyDirection() {
        return Direction.DOWN;
    }
}