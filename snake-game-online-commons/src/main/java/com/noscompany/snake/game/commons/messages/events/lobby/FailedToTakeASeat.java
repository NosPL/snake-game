package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.OnlineMessage;
import com.noscompany.snake.game.commons.messages.dto.GameLobbyState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToTakeASeat implements OnlineMessage {
    MessageType messageType = MessageType.FAILED_TO_TAKE_A_SEAT;
    Reason reason;
    GameLobbyState lobbyState;
    public enum Reason {
        USER_NOT_IN_THE_ROOM,
        GAME_ALREADY_RUNNING,
        SEAT_ALREADY_TAKEN;

    }

    public static FailedToTakeASeat userNotInTheRoom(GameLobbyState lobbyState) {
        return new FailedToTakeASeat(Reason.USER_NOT_IN_THE_ROOM, lobbyState);
    }

    public static FailedToTakeASeat gameAlreadyRunning(GameLobbyState lobbyState) {
        return new FailedToTakeASeat(Reason.GAME_ALREADY_RUNNING, lobbyState);
    }

    public static FailedToTakeASeat seatAlreadyTaken(GameLobbyState lobbyState) {
        return new FailedToTakeASeat(Reason.SEAT_ALREADY_TAKEN, lobbyState);
    }
}