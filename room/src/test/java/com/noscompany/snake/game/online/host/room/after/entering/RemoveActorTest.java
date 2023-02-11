package com.noscompany.snake.game.online.host.room.after.entering;

import org.junit.Test;

public class RemoveActorTest extends ActorEnteredTheRoomSetup {

    @Test
    public void actorShouldBeRemovedFromUsersList() {
//        GIVEN that the actor did not take a seat
        assert !room.userIsSitting(actorId);
//        WHEN someone tries to remove the actor from the room using his id
        var result = room.leave(actorId).get();
//        THEN he is removed from the users list
        assert !result.getUsersList().contains(actorName);
//        and 'player freed up a seat' event is not present
        assert result.getPlayerFreedUpASeat().isEmpty();
    }

    @Test
    public void actorThatTookASeatShouldBeRemovedFromTheUsersListAndShouldFreeUpASeat() {
//        GIVEN that the actor took a seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        WHEN someone tries to remove the actor from the room
        var result = room.leave(actorId).get();
//        THEN the actor is removed from the users list
        assert !result.getUsersList().contains(actorName);
//         and 'player freed up a seat' event is present
        assert result.getPlayerFreedUpASeat().isDefined();
    }
}