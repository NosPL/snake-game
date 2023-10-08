package com.noscompany.snake.online.user.registry.test;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import io.vavr.control.Either;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom.userAlreadyInTheRoom;

public final class EnteringRoomTest extends Setup {

    @Test
    public void actorWithUniqueNameShouldEnterTheNonFullRoom() {
//        GIVEN that the room is not full
        assert !userRegistry.isFull();
//        and the actor did not enter the room
        assert !userRegistry.hasUserWithId(actorId);
//        WHEN the actor tries to enter the room
        var result = userRegistry.enterRoom(actorId, actorName);
//        THEN he succeeds
        var expected = Either.right(new NewUserEnteredRoom(actorId, actorName.getName(), Set.of(actorName)));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToEnterTheRoomAgainWithTheSameName() {
//        GIVEN that some user already entered the room with an actor name
        assert userRegistry.enterRoom(UserId.random(), actorName).isRight();
//        WHEN the actor tries to enter the room with the same name
        var result = userRegistry.enterRoom(actorId, actorName);
//        THEN he fails due to name being already used
        var expected = Either.left(FailedToEnterRoom.userNameAlreadyInUse(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToEnterTheRoomWhenRoomIsFull() {
//        GIVEN that the room is full
        fillTheRoomWithRandomUsers();
        assert userRegistry.isFull();
//        WHEN the actor tries to enter the room
        var result = userRegistry.enterRoom(actorId, actorName);
//        THEN he fails due to room being full
        var expected = Either.left(FailedToEnterRoom.roomIsFull(actorId));
        Assert.assertEquals(expected, result);
    }

    private void fillTheRoomWithRandomUsers() {
        while (!userRegistry.isFull())
            userRegistry.enterRoom(UserId.random(), correctUniqueUsername());
    }

    @Test
    public void actorShouldFailToEnterTheRoomUsingBlankNames() {
        var expected = Either.left(FailedToEnterRoom.incorrectUserNameFormat(actorId));
        blankNames()
                .stream()
                .map(userName -> userRegistry.enterRoom(actorId, userName))
                .forEach(result -> Assert.assertEquals(expected, result));
    }

    @Test
    public void actorShouldFailToEnterTheRoomForTheSecondTimeWithADifferentName() {
//        WHEN the actor tries to enter the room again with a different name
        var result = userRegistry.enterRoom(actorId, correctUniqueUsername());
//        THEN he fails because he entered the room already
        var expected = Either.left(userAlreadyInTheRoom(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToEnterTheRoomForTheSecondTimeWithTheSameName() {
//        WHEN the actor tries to enter the room again with the same name
        var result = userRegistry.enterRoom(actorId, actorName);
//        THEN he fails due to being in the room already
        var expected = Either.left(userAlreadyInTheRoom(actorId));
        Assert.assertEquals(expected, result);
    }
}