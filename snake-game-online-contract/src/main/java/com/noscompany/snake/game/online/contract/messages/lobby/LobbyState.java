package com.noscompany.snake.game.online.contract.messages.lobby;

import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class LobbyState {
    GameOptions gameOptions;
    Set<Seat> seats;
    boolean gameRunning;
    GameState gameState;

    @Value
    @NoArgsConstructor(force = true, access = PRIVATE)
    @AllArgsConstructor
    public static class Seat {
        PlayerNumber playerNumber;
        Option<String> userName;
        boolean admin;
        boolean taken;
    }
}