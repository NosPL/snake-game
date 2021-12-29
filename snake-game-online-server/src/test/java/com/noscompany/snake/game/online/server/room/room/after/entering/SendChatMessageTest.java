package com.noscompany.snake.game.online.server.room.room.after.entering;

import com.noscompany.snake.game.commons.messages.events.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.server.room.room.commons.UserInTheRoomSetup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SendChatMessageTest extends UserInTheRoomSetup {

    @Test
    public void userShouldSendChatMessage() {
//        WHEN the user tries to send a chat message
        var messageContent = "sample message content";
        var result = room.sendChatMessage(userId, messageContent).get();
//        THEN he succeeds
        var expected = new UserSentChatMessage(userName, messageContent);
        assertEquals(expected, result);
    }
}