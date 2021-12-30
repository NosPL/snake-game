package com.noscompany.snake.game.commons.messages.dto;

import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class LobbyState {
    GridSize gridSize;
    GameSpeed gameSpeed;
    Walls walls;
    Option<LobbyAdmin> admin;
    Map<String, SnakeNumber> joinedPlayers;
    boolean gameRunning;
    GameState gameState;

    public Option<SnakeNumber> getAnyFreeSeat() {
        return Option.ofOptional(getFreeSeats().stream().findAny());
    }

    public List<SnakeNumber> getFreeSeats() {
        return Vector
                .of(SnakeNumber.values())
                .removeAll(joinedPlayers.values())
                .toJavaList();
    }

    public boolean userSeats(String username) {
        return joinedPlayers.containsKey(username);
    }

    public boolean seatIsFree(SnakeNumber snakeNumber) {
        return getFreeSeats().contains(snakeNumber);
    }

    public boolean seatIsTaken(SnakeNumber seatNumber) {
        return joinedPlayers.containsValue(seatNumber);
    }

    public boolean isAdmin(String userName) {
        return admin
                .map(LobbyAdmin::getUserName)
                .map(adminName -> adminName.equals(userName))
                .getOrElse(false);
    }

    @Value
    @NoArgsConstructor(force = true, access = PRIVATE)
    @AllArgsConstructor
    public static class Seat {
        SnakeNumber snakeNumber;
        Option<String> username;
        boolean admin;

        Seat markAsAdmin() {
            return new Seat(snakeNumber, username, true);
        }

        public boolean isFree() {
            return username.isEmpty();
        }

        public boolean isTaken() {
            return username.isDefined();
        }
    }
}