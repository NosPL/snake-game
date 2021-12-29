package com.noscompany.snake.game.online.server.room.message.handler;

import com.noscompany.snake.game.commons.messages.events.room.UserLeftRoom;
import com.noscompany.snake.game.online.server.room.message.handler.commons.BaseTestClass;
import io.vavr.control.Option;
import org.junit.Test;

public class RemovingUserTest extends BaseTestClass {

    @Test
    public void userRemovedEventShouldBeSentViaBroadcaster() {
//        given that the user entered the room
        enterTheRoom(userId, userName);
//        when someone tries to remove him from the room by his id
        removeUserById(userId);
//        than "UserRemoved" event is sent via broadcaster
        var expectedMessage = new UserLeftRoom(userName, roomUsers(), Option.none());
        assert getBroadcasterMessages().contains(expectedMessage);
//        and no direct message is sent to any resource
        assert getAllResourceMessages().isEmpty();
    }
}
