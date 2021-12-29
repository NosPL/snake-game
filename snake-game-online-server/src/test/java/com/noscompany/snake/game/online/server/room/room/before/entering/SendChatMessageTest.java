package com.noscompany.snake.game.online.server.room.room.before.entering;

import com.noscompany.snake.game.commons.messages.events.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.commons.messages.events.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.server.room.room.commons.UserNotInTheRoomSetup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SendChatMessageTest extends UserNotInTheRoomSetup {

    @Test
    public void userShouldFailToSendAChatMessage() {
//        WHEN the user tries to send a chat message
        var result = room.sendChatMessage(randomUserId(), "some message").getLeft();
//        THEN he fails due to not being in the room
        var expected = FailedToSendChatMessage.userIsNotInTheRoom();
        assertEquals(expected, result);
    }

    @Test
    public void userShouldSendChatMessage() {
//        GIVEN that the user entered the room
        assert room.enter(userId, userName).isRight();
//        WHEN the user tries to send a chat message
        var messageContent = "sample message content";
        var result = room.sendChatMessage(userId, messageContent).get();
//        THEN he succeeds
        var expected = new UserSentChatMessage(userName, messageContent);
        assertEquals(expected, result);
    }
}