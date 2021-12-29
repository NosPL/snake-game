package com.noscompany.snake.game.online.server.room.room.before.entering;

import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import com.noscompany.snake.game.commons.messages.events.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.server.room.room.commons.UserNotInTheRoomSetup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnterTheRoomTest extends UserNotInTheRoomSetup {

    @Test
    public void userWithUniqueNameShouldEnterTheNonFullRoom() {
//        GIVEN that the room is not full
        assert !roomIsFull();
//        and the room does not contain a user with certain name
        assert !roomContainsUserWithName(userName);
//        WHEN the user tries to enter the room
        var result = room.enter(userId, userName).get();
//        THEN he succeeds
        var expected = new NewUserEnteredRoom(userName, usersList());
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToEnterTheRoomWithAlreadyUsedName() {
//        GIVEN that some user already entered the room with certain name
        assert room.enter(randomUserId(), userName).isRight();
//        WHEN the user tries to enter the room with the same name
        var result = room.enter(userId, userName).getLeft();
//        THEN he fails due to name being already used
        var expected = FailedToEnterRoom.userNameAlreadyInUse(userName);
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToEnterTheRoomWhenRoomIsFull() {
//        GIVEN that the room is full
        fillTheRoomWithRandomUsers();
        assert roomIsFull();
//        WHEN another user tries to enter the room
        var result = room.enter(userId, userName).getLeft();
//        THEN he fails due to room being full
        var expected = FailedToEnterRoom.roomIsFull(userName);
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToEnterTheRoomUsingBlankName() {
//        GIVEN that a user wants to use blank name
        userName = "  ";
//        WHEN he tries to enter the room
        var result = room.enter(userId, userName).getLeft();
//        THEN he fails due to incorrect name format
        var expected = FailedToEnterRoom.incorrectUserNameFormat(userName);
        assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToEnterTheRoomUsingNameLongerThan15Signs() {
//        GIVEN that the user wants to use name longer than 15 signs
        userName = "aaabbbcccdddeeef";
        assert userName.codePoints().count() > 15;
//        WHEN he tries to enter the room
        var result = room.enter(userId, userName).getLeft();
//        THEN he fails due to incorrect name format
        var expected = FailedToEnterRoom.incorrectUserNameFormat(userName);
        assertEquals(expected, result);
    }
}
