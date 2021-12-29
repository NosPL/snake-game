package com.noscompany.snake.game.online.server.room.room.after.entering;

import com.noscompany.snake.game.online.server.room.room.commons.UserInTheRoomSetup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ChoosingAdminTest extends UserInTheRoomSetup {

    @Test
    public void userShouldBeChosenAsAdminAfterTakingASeatInTheEmptyLobby() {
//        given that the lobby is empty
        assert allSeatsAreFree();
//        and admin is not chosen
        assert !adminIsChosen();
//        when the user takes a seat
        room.takeASeat(userId, freeSeatNumber());
//        then he is chosen as the lobby admin
        assert adminNameEquals(userName);
    }

    @Test
    public void adminShouldNotChangeAfterTheOtherUserEntersTheLobby() {
//        given that the user is an admin
        room.takeASeat(userId, freeSeatNumber());
        assert adminNameEquals(userName);
//        when some other user takes a seat
        takeAnySeatWithRandomUser();
//        then the admin is the same
        assertEquals(userName, getAdminName());
    }

    @Test
    public void newAdminShouldBeChosenAfterTheCurrentOneFreesUpASeat() {
//        given that the user got chosen as an admin
        room.takeASeat(userId, freeSeatNumber());
        assert adminNameEquals(userName);
//        and someone took a seat
        takeAnySeatWithRandomUser();
//        when the user frees up a seat
        assert room.freeUpASeat(userId).isRight();
//        then some other admin is chosen
        assert adminIsChosen();
        assertNotEquals(userName, getAdminName());
    }

    @Test
    public void adminSeatNumberShouldUpdateWhenTheAdminChangesTheSeat() {
//        given that the user got chosen as an admin
        var firstSeatNumber = freeSeatNumber();
        room.takeASeat(userId, firstSeatNumber);
        assert adminNameEquals(userName);
//        when he changes the seat
        var secondSeatNumber = freeSeatOtherThan(firstSeatNumber).get();
        assert room.takeASeat(userId, secondSeatNumber).isRight();
//        he is still the admin
        assertEquals(userName, getAdminName());
    }

    @Test
    public void roomShouldHaveNoAdminAfterTheOnlyUserFreesUpASeat() {
//        given that only one seat in the lobby is taken
        room.takeASeat(userId, freeSeatNumber());
        assert numberOfTakenSeats() == 1;
//        and the user is chosen as an admin
        assert adminNameEquals(userName);
//        when he frees up a seat
        room.freeUpASeat(userId);
//        then there is no admin in the lobby
        assert !adminIsChosen();
    }
}