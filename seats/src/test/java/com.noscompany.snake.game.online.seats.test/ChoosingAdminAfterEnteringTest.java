package com.noscompany.snake.game.online.seats.test;

import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.Seat;
import com.noscompany.snake.game.online.seats.test.commons.AfterEnteringRoom;
import io.vavr.control.Either;
import org.junit.Test;

public class ChoosingAdminAfterEnteringTest extends AfterEnteringRoom {

    @Test
    public void thereShouldBeNoAdminWhenAllSeatsAreFree() {
//        WHEN all seats are free
        assert allSeatsAreFree();
//        THEN admin is not chosen
        assert !adminIsChosen();
    }

    @Test
    public void actorShouldBeChosenAsAdminAfterTakingASeatInTheEmptyLobby() {
//        GIVEN that all seats are free
        assert allSeatsAreFree();
//        WHEN the actor successfully takes a seat
        assert isSuccess(seats.takeOrChangeSeat(actorId, freeSeatNumber()));
//        THEN he is the admin
        assert seats.userIsAdmin(actorId);
    }

    @Test
    public void roomShouldHaveNoAdminWhenAllSeatsBecomeFree() {
//        GIVEN that the actor took a seat
        assert isSuccess(seats.takeOrChangeSeat(actorId, freeSeatNumber()));
//        and the actor is the admin
        assert seats.userIsAdmin(actorId);
//        WHEN the actor frees up a seat
        var either = seats.freeUpSeat(actorId);
        assert isSuccess(either);
//        THEN there is no admin
        assert !adminIsChosen();
    }

    @Test
    public void adminShouldNotChangeIfOtherUserTakesSeat() {
//        GIVEN that the actor took a seat
        assert isSuccess(seats.takeOrChangeSeat(actorId, freeSeatNumber()));
//        and the actor is the admin
        assert seats.userIsAdmin(actorId);
//        WHEN some other user takes a seat
        assert isSuccess(someRandomUserTakesASeat());
//        THEN the actor is still the admin
        assert seats.userIsAdmin(actorId);
    }



    @Test
    public void newAdminShouldBeChosenIfTheCurrentOneFreesUpASeat() {
//        GIVEN that the actor took a seat
        assert isSuccess(seats.takeOrChangeSeat(actorId, freeSeatNumber()));
//        and the actor is the admin
        assert seats.userIsAdmin(actorId);
//        and some other user took a seat
        assert isSuccess(someRandomUserTakesASeat());
//        WHEN the actor frees up a seat
        assert isSuccess(seats.freeUpSeat(actorId));
//        THEN some other admin is chosen
        assert adminIsChosen();
//        and it's not the actor
        assert !seats.userIsAdmin(actorId);
    }

    @Test
    public void adminShouldNotChangeAfterChangingSeat() {
//        GIVEN that the actor took a seat
        assert isSuccess(seats.takeOrChangeSeat(actorId, freeSeatNumber()));
//        and the actor is the admin
        assert seats.userIsAdmin(actorId);
//        and some other user took a seat
        assert isSuccess(someRandomUserTakesASeat());
//        WHEN the actor changes the seat
        assert isSuccess(seats.takeOrChangeSeat(actorId, freeSeatNumber()));
//        THEN the actor is still the admin
        assert seats.userIsAdmin(actorId);
    }

    private boolean allSeatsAreFree() {
        return seats
                .toDto().stream()
                .noneMatch(Seat::isTaken);
    }
}