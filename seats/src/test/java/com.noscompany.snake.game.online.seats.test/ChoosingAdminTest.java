package com.noscompany.snake.game.online.seats.test;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.seats.Seat;
import org.junit.Before;
import org.junit.Test;

public class ChoosingAdminTest extends TestSetup {

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
        assert isSuccess(seats.freeUpSeat(actorId));
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
        assert isSuccess(randomUserTakesASeat());
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
        UserId user = UserId.random();
        assert isSuccess(randomUserTakesASeat(user));
//        WHEN the actor frees up a seat
        assert isSuccess(seats.freeUpSeat(actorId));
//        THEN the other use is admin
        assert !seats.userIsAdmin(actorId);
    }

    @Test
    public void adminShouldNotChangeAfterChangingSeat() {
//        GIVEN that the actor took a seat
        assert isSuccess(seats.takeOrChangeSeat(actorId, freeSeatNumber()));
//        and the actor is the admin
        assert seats.userIsAdmin(actorId);
//        and some other user took a seat
        assert isSuccess(randomUserTakesASeat());
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

    @Before
    public void AfterEnteringRoomInit() {
    }
}