package com.noscompany.snake.game.online.server.room.room.after.entering;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToTakeASeat;
import com.noscompany.snake.game.commons.messages.events.lobby.PlayerTookASeat;
import com.noscompany.snake.game.online.server.room.room.commons.UserInTheRoomSetup;
import org.junit.Test;
import snake.game.core.dto.SnakeNumber;

import static org.junit.Assert.assertEquals;

public class TakeASeatTest extends UserInTheRoomSetup {

    @Test
    public void userShouldTakeAFreeLobbySeat() {
//        given that the user did not take a seat
        assert !tookASeat(userName);
//        when he tries to take any free seat
        var freeSeatNumber = getFreeLobbySeat().get();
        var result = room.takeASeat(userId, freeSeatNumber).get();
//        then he succeeds
        var expected = new PlayerTookASeat(userName, freeSeatNumber, lobbyState());
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToTakeASeatThatIsAlreadyTaken() {
//        given that the seat is taken
        SnakeNumber seatNumber = freeSeatNumber();
        assert takeASeatWithRandomUser(seatNumber).isRight();
//        when the user tries to take this seat
        var result = room.takeASeat(userId, seatNumber).getLeft();
//        then he fails due to seat being already taken
        var expected = FailedToTakeASeat.seatAlreadyTaken(lobbyState());
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToTakeAFreeSeatWhileGameIsRunning() {
//        given that someone already started game
        room.takeASeat(userId, freeSeatNumber());
        room.startGame(userId);
        assert gameIsRunning();
//        when someone tries to take the seat
        var result = takeAnySeatWithRandomUser().getLeft();
//        then he fails because game is running
        var expected = FailedToTakeASeat.gameAlreadyRunning(lobbyState());
        assertEquals(expected, result);
    }
}