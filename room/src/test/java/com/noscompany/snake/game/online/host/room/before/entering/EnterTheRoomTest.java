package com.noscompany.snake.game.online.host.room.before.entering;

import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import org.junit.Assert;
import org.junit.Test;

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
        var expected = success(new NewUserEnteredRoom(actorId, actorName.getName(), room.getState()));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToEnterTheRoomAgainWithTheSameName() {
//        GIVEN that some user already entered the room with an actor name
        assert isSuccess(room.enter(randomUserId(), actorName));
//        WHEN the actor tries to enter the room with the same name
        var result = room.enter(actorId, actorName);
//        THEN he fails due to name being already used
        var expected = failure(FailedToEnterRoom.userNameAlreadyInUse(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToEnterTheRoomWhenRoomIsFull() {
//        GIVEN that the room is full
        fillTheRoomWithRandomUsers();
        assert room.isFull();
//        WHEN the actor tries to enter the room
        var result = room.enter(actorId, actorName);
//        THEN he fails due to room being full
        var expected = failure(FailedToEnterRoom.roomIsFull(actorId));
        Assert.assertEquals(expected, result);
    }

    private void fillTheRoomWithRandomUsers() {
        while (!room.isFull())
            room.enter(randomUserId(), randomValidUserName());
    }

    @Test
    public void actorShouldFailToEnterTheRoomUsingBlankName() {
//        GIVEN that the actor wants to use blank name
        actorName = new UserName("  ");
//        WHEN he tries to enter the room
        var result = room.enter(actorId, actorName);
//        THEN he fails due to incorrect name format
        var expected = failure(FailedToEnterRoom.incorrectUserNameFormat(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToEnterTheRoomUsingNameLongerThan15Signs() {
//        GIVEN that the actor wants to use name longer than 15 signs
        actorName = new UserName("aaabbbcccdddeeef");
        assert actorName.getName().codePoints().count() > 15;
//        WHEN he tries to enter the room
        var result = room.enter(actorId, actorName);
//        THEN he fails due to incorrect name format
        var expected = failure(FailedToEnterRoom.incorrectUserNameFormat(actorId));
        Assert.assertEquals(expected, result);
    }
}