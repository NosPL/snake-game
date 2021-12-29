package com.noscompany.snake.game.online.server.room.room.after.entering;

import com.noscompany.snake.game.online.server.room.room.commons.UserInTheRoomSetup;
import org.junit.Test;

public class RemoveUserTest extends UserInTheRoomSetup {

    @Test
    public void userShouldBeRemovedFromUsersList() {
//        GIVEN that the user did not take a seat
        assert !userWithNameTookASeat(userName);
//        WHEN someone tries to remove the user from the room using his id
        var result = room.removeUserById(userId).get();
//        THEN he is removed from the users list
        assert !result.getUsersList().contains(userName);
//        and the user did not free up a seat
        assert result.getPlayerFreedUpASeat().isEmpty();
    }

    @Test
    public void userThatTookASeatShouldBeRemovedFromTheUsersListAndShouldFreeUpASeat() {
//        GIVEN that the user took a seat
        assert room.takeASeat(userId, freeSeatNumber()).isRight();
//        WHEN someone tries to remove the user from the room using his id
        var result = room.removeUserById(userId).get();
//        THEN he is removed from the users list
        assert !result.getUsersList().contains(userName);
//        and the user freed up a seat
        assert result.getPlayerFreedUpASeat().isDefined();
    }
}