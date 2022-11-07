package com.noscompany.snake.game.online.host.room.room.before.entering;

import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class EnterTheRoomTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorWithUniqueNameShouldEnterTheNonFullRoom() {
//        GIVEN that the room is not full
        assert !room.isFull();
//        and the actor did not enter the room
        assert !room.hasUserWithId(actorId);
//        WHEN the actor tries to enter the room
        var result = room.enter(actorId, actorName);
//        THEN he succeeds
        var expected = success(newUserEnteredRoom(actorName));
        assertEquals(expected, result);
    }

    private NewUserEnteredRoom newUserEnteredRoom(String userName) {
        Set<String> connectedUsers = room.getState().getUsers();
        return new NewUserEnteredRoom(userName, connectedUsers);
    }

    @Test
    public void actorShouldFailToEnterTheRoomAgainWithTheSameName() {
//        GIVEN that some user already entered the room with an actor name
        assert isSuccess(room.enter(randomUserId(), actorName));
//        WHEN the actor tries to enter the room with the same name
        var result = room.enter(actorId, actorName);
//        THEN he fails due to name being already used
        var expected = failure(FailedToEnterRoom.userNameAlreadyInUse(actorName));
        assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToEnterTheRoomWhenRoomIsFull() {
//        GIVEN that the room is full
        fillTheRoomWithRandomUsers();
        assert room.isFull();
//        WHEN the actor tries to enter the room
        var result = room.enter(actorId, actorName);
//        THEN he fails due to room being full
        var expected = failure(FailedToEnterRoom.roomIsFull(actorName));
        assertEquals(expected, result);
    }

    private void fillTheRoomWithRandomUsers() {
        while (!room.isFull())
            room.enter(randomUserId(), randomValidUserName());
    }

    @Test
    public void actorShouldFailToEnterTheRoomUsingBlankName() {
//        GIVEN that the actor wants to use blank name
        actorName = "  ";
//        WHEN he tries to enter the room
        var result = room.enter(actorId, actorName);
//        THEN he fails due to incorrect name format
        var expected = failure(FailedToEnterRoom.incorrectUserNameFormat(actorName));
        assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToEnterTheRoomUsingNameLongerThan15Signs() {
//        GIVEN that the actor wants to use name longer than 15 signs
        actorName = "aaabbbcccdddeeef";
        assert actorName.codePoints().count() > 15;
//        WHEN he tries to enter the room
        var result = room.enter(actorId, actorName);
//        THEN he fails due to incorrect name format
        var expected = failure(FailedToEnterRoom.incorrectUserNameFormat(actorName));
        assertEquals(expected, result);
    }
}