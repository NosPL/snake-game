package com.noscompany.snake.game.online.server.room.message.handler;

import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import com.noscompany.snake.game.commons.messages.events.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.server.room.message.handler.commons.BaseTestClass;
import org.junit.Test;

public class EnteringTheRoomTest extends BaseTestClass {

    @Test
    public void newUserEnteredTheRoomEventShouldBeSentViaBroadcaster() {
//        when the user enters the room
        enterTheRoom(userId, userName);
//        then "NewUserEnteredRoom" event is sent via broadcaster
        var expectedMessage = new NewUserEnteredRoom(userName, roomUsers());
        assert getBroadcasterMessages().contains(expectedMessage);
//        and no message is sent to any resource
        assert getAllResourceMessages().isEmpty();
    }

    @Test
    public void failedToEnterTheRoomEventShouldBeSentToTheUserResourceOnly() {
//        given that someone already entered the room using certain name
        var userName = randomValidUserName();
        enterTheRoom(randomUserId(), userName);
//        when the user tries to enter the room using the same name
        enterTheRoom(userId, userName);
//        then "FailedToEnterTheRoom" event should be sent to user's resource
        var expectedMessage = FailedToEnterRoom.userNameAlreadyInUse(userName);
        assert getResourceMessagesById(userId).contains(expectedMessage);
    }
}