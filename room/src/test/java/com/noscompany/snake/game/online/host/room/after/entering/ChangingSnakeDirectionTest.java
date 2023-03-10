package com.noscompany.snake.game.online.host.room.after.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToCancelGame;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToChangeSnakeDirection;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.host.room.before.entering.ActorNotInTheRoomSetup;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class ChangingSnakeDirectionTest extends ActorEnteredTheRoomSetup {

    @Test
    public void changingSnakeDirectionShouldNotReturnFailure() {
//        GIVEN that actor took a seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        AND game is started
        isSuccess(room.startGame(actorId));
//        WHEN the actor tries to change snake direction
        var result = room.changeSnakeDirection(actorId, anyDirection());
//        THEN he succeeds
        var expected = Option.none();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToChangeDirectionWithoutTakingASeat() {
//        WHEN the actor tries to change direction
        var result = room.changeSnakeDirection(actorId, anyDirection());
//        THEN he fails due to not taking a seat
        var expected = Option.of(FailedToChangeSnakeDirection.playerDidNotTakeASeat());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToChangeSnakeDirectionWhenGameIsNotStarted() {
//        GIVEN that actor took a seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        WHEN the actor tries to change snake direction
        var result = room.changeSnakeDirection(actorId, anyDirection());
//        THEN he fails because game is not running
        var expected = Option.of(FailedToChangeSnakeDirection.gameNotStarted());
        Assert.assertEquals(expected, result);
    }

    private Direction anyDirection() {
        return Direction.DOWN;
    }
}