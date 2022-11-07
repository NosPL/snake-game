package com.noscompany.snake.game.online.host.server.message.handler;

import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.host.server.message.handler.commons.BaseTestClass;
import org.junit.Test;

public class SendingChatMessageTest extends BaseTestClass {

    @Test
    public void chatMessageReceivedEventShouldBeSentViaBroadcaster() {
//        given that the user entered the room
        enterTheRoom(userId, userName);
//        when he tries to send a chat message
        var messageContent = "some content";
        sendChatMessage(userId, messageContent);
//        then "ChatMessageReceived" event is sent via broadcaster
        var expectedMessage = new UserSentChatMessage(userName, messageContent);
        assert getBroadcasterMessages().contains(expectedMessage);
//        and no direct message is sent to any resource
        assert getAllResourceMessages().isEmpty();
    }

    @Test
    public void failedToSendChatMessageShouldBeSentToTheUserResourceOnly() {
//        when user tries to send a chat message
        var messageContent = "some content";
        sendChatMessage(userId, messageContent);
//        then "FailedToSendChatMessage" event is sent to the user's resource
        var expectedMessage = FailedToSendChatMessage.userIsNotInTheRoom();
        assert getResourceMessagesById(userId).contains(expectedMessage);
//        and no message is sent via broadcaster
        assert getBroadcasterMessages().isEmpty();
    }
}