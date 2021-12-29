package com.noscompany.snake.game.online.server.room.message.handler;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToTakeASeat;
import com.noscompany.snake.game.commons.messages.events.lobby.PlayerTookASeat;
import com.noscompany.snake.game.online.server.room.message.handler.commons.BaseTestClass;
import org.junit.Test;

public class TakingASeatTest extends BaseTestClass {

    @Test
    public void userTookASeatEventShouldBeSentViaBroadcaster() {
//        given that the user entered the room
        enterTheRoom(userId, userName);
//        when he tries to take a free seat
        var seatNumber = anyFreeSeatNumber();
        takeASeat(userId, seatNumber);
//        then "PlayerTookASeat" event is sent via broadcaster
        var expectedMessage = new PlayerTookASeat(userName, seatNumber, lobbyState());
        assert getBroadcasterMessages().contains(expectedMessage);
//        and no direct message is sent to any resource
        assert getAllResourceMessages().isEmpty();
    }

    @Test
    public void failedToTakeASeatShouldBeSentToUserResourceOnly() {
//        when the user tries to take a free seat
        takeASeat(userId, anyFreeSeatNumber());
//        then "FailedToTakeAFreeSeat" event is sent to user's resource
        var expectedMessage = FailedToTakeASeat.userNotInTheRoom(lobbyState());
        assert getResourceMessagesById(userId).contains(expectedMessage);
//        and no message is sent via broadcaster
        assert getBroadcasterMessages().isEmpty();
    }
}