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
        room.newUserEnteredRoom(actorId, actorName);
    }

    protected Either<FailedToTakeASeat, PlayerTookASeat> someRandomUserTakesASeat() {
        return takeASeatWithRandomUser(freeSeatNumber());
    }

    protected Either<FailedToTakeASeat, PlayerTookASeat> takeASeatWithRandomUser(PlayerNumber seatNumber) {
        var randomUserId = randomUserId();
        room.newUserEnteredRoom(randomUserId, randomValidUserName());
        return room.takeASeat(randomUserId, seatNumber);
    }

    protected boolean isSuccess(Option<FailedToStartGame> startGameResult) {
        return startGameResult.isEmpty();
    }
}