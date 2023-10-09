package com.noscompany.snake.game.online.playground.test.after.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.playground.test.commons.PlaygroundTestSetup;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.Before;

public class ActorEnteredTheRoomSetup extends PlaygroundTestSetup {

    @Before
    public void enterTheRoom() {
        playground.newUserEnteredRoom(actorId, actorName);
    }

    protected Either<FailedToTakeASeat, PlayerTookASeat> someRandomUserTakesASeat() {
        return takeASeatWithRandomUser(freeSeatNumber());
    }

    protected Either<FailedToTakeASeat, PlayerTookASeat> takeASeatWithRandomUser(PlayerNumber seatNumber) {
        var randomUserId = randomUserId();
        playground.newUserEnteredRoom(randomUserId, randomValidUserName());
        return playground.takeASeat(randomUserId, seatNumber);
    }

    protected boolean isSuccess(Option<FailedToStartGame> startGameResult) {
        return startGameResult.isEmpty();
    }
}