package com.noscompany.snake.game.online.server.room.room.commons;

import com.noscompany.snake.game.commons.messages.dto.LobbyAdmin;
import com.noscompany.snake.game.commons.messages.dto.LobbyState;
import com.noscompany.snake.game.commons.messages.events.lobby.FailedToTakeASeat;
import com.noscompany.snake.game.commons.messages.events.lobby.PlayerTookASeat;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.Before;
import snake.game.core.dto.SnakeNumber;

import java.util.Optional;
import java.util.UUID;

public class UserInTheRoomSetup extends RoomTestSetup {

    @Before
    public void enterTheRoom() {
        assert room.enter(userId, userName).isRight();
    }

    protected Option<SnakeNumber> freeSeatOtherThan(SnakeNumber excludedSeat) {
        Optional<SnakeNumber> result = lobbyState()
                .getFreeSeats()
                .stream()
                .filter(seatNumber -> !seatNumber.equals(excludedSeat))
                .findAny();
        return Option.ofOptional(result);
    }

    protected Option<LobbyAdmin> getAdmin() {
        return lobbyState().getAdmin();
    }

    protected Option<String> adminName() {
        return lobbyState()
                .getAdmin()
                .map(LobbyAdmin::getUserName);
    }

    protected int numberOfTakenSeats() {
        return lobbyState()
                .getJoinedPlayers()
                .size();
    }

    protected boolean allSeatsAreFree() {
        return lobbyState().getJoinedPlayers().isEmpty();
    }

    protected void takeASeatAndStartGame(String username) {
        room.takeASeat(username, freeSeatNumber());
        room.startGame(username);
    }

    protected boolean gameIsRunning() {
        return lobbyState().isGameRunning();
    }

    protected String randomName() {
        return UUID.randomUUID().toString();
    }

    protected LobbyState lobbyState() {
        return room.getState().getLobbyState();
    }

    protected boolean isAdmin(String userName) {
        return lobbyState()
                .isAdmin(userName);
    }

    protected SnakeNumber changeSeatToAnyOtherThan(SnakeNumber firstSeatNumber) {
        var nextLobbySeatNumber = freeSeatOtherThan(firstSeatNumber).get();
        assert room.takeASeat(userName, nextLobbySeatNumber).isRight();
        return nextLobbySeatNumber;
    }

    protected SnakeNumber changeSeatOfUserWithName(String userName) {
        var currentSeatNumber = room.getState().getLobbyState().getJoinedPlayers().get(userName);
        var newSeatNumber = freeSeatOtherThan(currentSeatNumber).get();
        assert room.takeASeat(userName, newSeatNumber).isRight();
        return newSeatNumber;
    }

    protected SnakeNumber getAdminSeatNumber() {
        return getAdmin().get().getSnakeNumber();
    }

    protected boolean adminIsChosen() {
        return lobbyState().getAdmin().isDefined();
    }

    protected Option<SnakeNumber> getFreeLobbySeat() {
        return lobbyState().getAnyFreeSeat();
    }

    protected boolean seatIsTaken(SnakeNumber seatNumber) {
        return lobbyState().seatIsTaken(seatNumber);
    }

    protected boolean tookASeat(String userName) {
        return lobbyState().userSeats(userName);
    }

    protected boolean adminNameEquals(String userName) {
        return adminName()
                .map(adminName -> adminName.equals(userName))
                .getOrElse(false);
    }

    protected Either<FailedToTakeASeat, PlayerTookASeat> takeAnySeatWithRandomUser() {
        return takeASeatWithRandomUser(freeSeatNumber());
    }

    protected Either<FailedToTakeASeat, PlayerTookASeat> takeASeatWithRandomUser(SnakeNumber seatNumber) {
        String randomUserId = randomUserId();
        String randomUserName = randomValidUserName();
        assert room.enter(randomUserId, randomUserName).isRight();
        var result = room.takeASeat(randomUserId, seatNumber);
        return result;
    }

    protected String getAdminName() {
        return adminName().get();
    }
}