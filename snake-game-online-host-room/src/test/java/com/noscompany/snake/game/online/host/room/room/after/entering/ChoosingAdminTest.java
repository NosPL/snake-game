package com.noscompany.snake.game.online.host.room.room.after.entering;

import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import org.junit.Test;

public class ChoosingAdminTest extends ActorEnteredTheRoomSetup {

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
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        THEN he is the admin
        assert room.userIsAdmin(actorId);
    }

    @Test
    public void roomShouldHaveNoAdminWhenAllSeatsBecomeFree() {
//        GIVEN that the actor took a seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        and the actor is the admin
        assert room.userIsAdmin(actorId);
//        WHEN the actor frees up a seat
        assert isSuccess(room.freeUpASeat(actorId));
//        THEN there is no admin
        assert !adminIsChosen();
    }

    @Test
    public void adminShouldNotChangeIfOtherUserTakesSeat() {
//        GIVEN that the actor took a seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        and the actor is the admin
        assert room.userIsAdmin(actorId);
//        WHEN some other user takes a seat
        assert isSuccess(someRandomUserTakesASeat());
//        THEN the actor is still the admin
        assert room.userIsAdmin(actorId);
    }

    @Test
    public void newAdminShouldBeChosenIfTheCurrentOneFreesUpASeat() {
//        GIVEN that the actor took a seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        and the actor is the admin
        assert room.userIsAdmin(actorId);
//        and some other user took a seat
        assert isSuccess(someRandomUserTakesASeat());
//        WHEN the actor frees up a seat
        assert isSuccess(room.freeUpASeat(actorId));
//        THEN some other admin is chosen
        assert adminIsChosen();
//        and it's not the actor
        assert !room.userIsAdmin(actorId);
    }

    @Test
    public void adminShouldNotChangeAfterChangingSeat() {
//        GIVEN that the actor took a seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        and the actor is the admin
        assert room.userIsAdmin(actorId);
//        and some other user took a seat
        assert isSuccess(someRandomUserTakesASeat());
//        WHEN the actor changes the seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        THEN the actor is still the admin
        assert room.userIsAdmin(actorId);
    }

    private boolean allSeatsAreFree() {
        return lobbyState()
                .getSeats().stream()
                .noneMatch(LobbyState.Seat::isTaken);
    }
}