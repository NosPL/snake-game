package com.noscompany.snake.game.online.gameplay.supervisor.test;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToChangeSnakeDirection;
import com.noscompany.snake.game.online.contract.messages.seats.AdminId;
import com.noscompany.snake.game.online.gameplay.supervisor.test.commons.PlaygroundTestSetup;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class ChangingSnakeDirectionTest extends PlaygroundTestSetup {

    @Test
    public void changeSnakeDirectionHappyPath() {
//        GIVEN that actor took a seat as admin
        gameplaySupervisor.playerTookASeat(actorId, anyPlayerNumber(), AdminId.of(actorId));
//        AND game is started
        isSuccess(gameplaySupervisor.startGame(actorId));
//        WHEN the actor tries to change snake direction
        var result = gameplaySupervisor.changeSnakeDirection(actorId, anyDirection());
//        THEN he succeeds
        var expected = Option.none();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToChangeSnakeDirectionWhenGameIsNotStarted() {
//        GIVEN that actor took a seat as admin
        gameplaySupervisor.playerTookASeat(actorId, anyPlayerNumber(), AdminId.of(actorId));
//        WHEN he tries to change snake direction
        var result = gameplaySupervisor.changeSnakeDirection(actorId, anyDirection());
//        THEN he fails because game is not running
        var expected = Option.of(FailedToChangeSnakeDirection.gameNotStarted(actorId));
        Assert.assertEquals(expected, result);
    }

    private Direction anyDirection() {
        return Direction.DOWN;
    }
}