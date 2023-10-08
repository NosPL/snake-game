package com.noscompany.snake.game.online.chat;

import com.noscompany.message.publisher.utils.NullMessagePublisher;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import io.vavr.control.Either;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.vavr.control.Either.left;
import static org.junit.Assert.assertEquals;

public final class ChatTest {
    private Chat chat;

    @Before
    public void init() {
        chat = new ChatConfiguration().createChat(new NullMessagePublisher());
    }

    @Test
    public void userThatDidNotEnterRoomShouldFail() {
//        WHEN user tries to send a chat message without entering the room first
        UserId userId = UserId.random();
        var result = chat.sendMessage(userId, "msg content");
//        THEN he fails due to not being in the room
        var expected = left(FailedToSendChatMessage.userIsNotInTheRoom(userId));
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToSendChatMessageAfterLeavingRoom() {
//        GIVEN that user entered room
        var userId = UserId.random();
        chat.newUserEnteredRoom(userId, UserName.random());
//        AND user left the room
        chat.userLeftRoom(userId);
//        WHEN user tries to send a chat message
        var result = chat.sendMessage(userId, "msg content");
//        THEN he fails due to not being in the room
        var expected = left(FailedToSendChatMessage.userIsNotInTheRoom(userId));
        assertEquals(expected, result);
    }

    @Test
    public void SendChatMessageHappyPath() {
//        GIVEN that user entered room
        var userId = UserId.random();
        var userName = UserName.random();
        chat.newUserEnteredRoom(userId, userName);
//        WHEN user tries to send a chat message
        var messageContent = "sample message content";
        var result = chat.sendMessage(userId, messageContent);
//        THEN he succeeds
        var expected = Either.right(new UserSentChatMessage(userId, userName.getName(), messageContent));
        Assert.assertEquals(expected, result);
    }
}