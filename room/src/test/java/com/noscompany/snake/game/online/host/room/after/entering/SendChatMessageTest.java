package com.noscompany.snake.game.online.host.room.after.entering;

import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SendChatMessageTest extends ActorEnteredTheRoomSetup {

    @Test
    public void actorShouldSendChatMessage() {
//        WHEN the actor tries to send a chat message
        var messageContent = "sample message content";
        var result = room.sendChatMessage(actorId, messageContent);
//        THEN he succeeds
        var expected = success(userSentChatMessage(actorName.getName(), messageContent));
        Assert.assertEquals(expected, result);
    }

    private UserSentChatMessage userSentChatMessage(String userName, String messageContent) {
        return new UserSentChatMessage(userName, messageContent);
    }
}