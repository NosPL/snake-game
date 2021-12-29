package com.noscompany.snake.game.online.server.room.room.commons;

import com.noscompany.snake.game.commons.messages.dto.LobbyState;
import com.noscompany.snake.game.online.server.room.room.Room;
import com.noscompany.snake.game.online.server.room.room.RoomCreator;
import io.vavr.control.Option;
import org.junit.Before;
import snake.game.core.dto.SnakeNumber;

import java.util.Set;
import java.util.UUID;

public class RoomTestSetup {
    protected Room room;
    protected String userId;
    protected String userName;

    @Before
    public void init() {
        room = RoomCreator.create(new SnakeGameNullHandler(), new EndlesslyRunningGameConfiguration());
        userId = randomUserId();
        userName = randomValidUserName();
    }

    protected void fillTheRoomWithRandomUsers() {
        while (!roomIsFull())
            room.enter(randomUserId(), randomValidUserName());
    }

    protected String randomUserId() {
        return UUID.randomUUID().toString();
    }

    protected String randomValidUserName() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    protected LobbyState getLobbyState() {
        return room.getState().getLobbyState();
    }

    protected boolean roomContainsUserWithName(String userName) {
        return room.getState().getUsers().contains(userName);
    }

    protected boolean roomIsFull() {
        return room.getState().isFull();
    }

    protected Set<String> usersList() {
        return room.getState().getUsers();
    }

    protected SnakeNumber freeSeatNumber() {
        return getLobbyState().getAnyFreeSeat().get();
    }

    protected boolean userWithNameTookASeat(String userName) {
        return getLobbyState().userSeats(userName);
    }
}