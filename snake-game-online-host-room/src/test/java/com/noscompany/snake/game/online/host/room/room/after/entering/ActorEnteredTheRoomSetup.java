package com.noscompany.snake.game.online.host.room.room.after.entering;

import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.lobby.event.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.host.room.room.commons.RoomTestSetup;
import io.vavr.control.Either;
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
}