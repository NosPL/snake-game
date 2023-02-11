package com.noscompany.snake.game.online.host.room.before.entering;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SendChatMessageTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToSendAChatMessage() {
//        WHEN the actor tries to send a chat message
        var result = room.sendChatMessage(randomUserId(), "some message");
//        THEN he fails due to not being in the room
        var expected = failure(FailedToSendChatMessage.userIsNotInTheRoom());
        assertEquals(expected, result);
    }
}