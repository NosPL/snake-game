package com.noscompany.snake.game.online.host.room.after.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.host.room.commons.RoomTestSetup;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.Before;

public class ActorEnteredTheRoomSetup extends RoomTestSetup {

    @Before
    public void enterTheRoom() {
        assert isSuccess(room.enter(actorId, actorName));
    }

    protected Either<FailedToTakeASeat, PlayerTookASeat> someRandomUserTakesASeat() {
        return takeASeatWithRandomUser(freeSeatNumber());
    }

    protected Either<FailedToTakeASeat, PlayerTookASeat> takeASeatWithRandomUser(PlayerNumber seatNumber) {
        var randomUserId = randomUserId();
        assert isSuccess(room.enter(randomUserId, randomValidUserName()));
        var result = room.takeASeat(randomUserId, seatNumber);
        return result;
    }

    protected boolean isSuccess(Option<FailedToStartGame> startGameResult) {
        return startGameResult.isEmpty();
    }
}