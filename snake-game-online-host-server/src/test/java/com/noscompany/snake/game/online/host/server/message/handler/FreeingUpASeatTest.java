package com.noscompany.snake.game.online.host.server.message.handler;

import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.lobby.event.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.host.server.message.handler.commons.BaseTestClass;
import org.junit.Test;

public class FreeingUpASeatTest extends BaseTestClass {

    @Test
    public void userFreedUpASeatEventShouldBeSentViaBroadcaster() {
//        given that the user entered the room
        enterTheRoom(userId, userName);
//        and he took a seat
        var seatNumber = anyFreeSeatNumber();
        takeASeat(userId, seatNumber);
//        when he tries to free up a seat
        freeUpASeat(userId);
//        then "PlayerFreedUpASeat" event is sent via broadcaster
        var expectedMessage = new PlayerFreedUpASeat(userName, seatNumber, lobbyState());
        assert getBroadcasterMessages().contains(expectedMessage);
//        and no message is sent directly to any resource
        assert getAllResourceMessages().isEmpty();
    }

    @Test
    public void failedToFreeUpASeatShouldBeSentToTheUserResourceOnly() {
//        when the user tries to free up a seat
        freeUpASeat(userId);
//        then "FailedToFreeUpASeat" event is sent to user resource
        var expectedMessage = FailedToFreeUpSeat.userNotInTheRoom();
        assert getResourceMessagesById(userId).contains(expectedMessage);
//        and no message is sent via broadcaster
        assert getBroadcasterMessages().isEmpty();
    }
}