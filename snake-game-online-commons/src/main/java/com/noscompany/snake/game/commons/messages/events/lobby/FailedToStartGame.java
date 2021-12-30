package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.OnlineMessage;
import com.noscompany.snake.game.commons.messages.dto.LobbyState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToStartGame implements OnlineMessage {
    MessageType messageType = MessageType.FAILED_TO_START_GAME;
    Reason reason;
    LobbyState lobbyState;

    public static FailedToStartGame requesterDidNotTookASeat(LobbyState lobbyState) {
        return new FailedToStartGame(Reason.REQUESTER_IS_NOT_JOINED_TO_GAME, lobbyState);
    }

    public static FailedToStartGame gameIsAlreadyRunning(LobbyState lobbyState) {
        return new FailedToStartGame(Reason.GAME_IS_ALREADY_RUNNING, lobbyState);
    }

    public static FailedToStartGame requesterIsNotAdmin(LobbyState lobbyState) {
        return new FailedToStartGame(Reason.REQUESTER_IS_NOT_ADMIN, lobbyState);
    }

    public static FailedToStartGame userIsNotInTheRoom(LobbyState lobbyState) {
        return new FailedToStartGame(Reason.USER_IS_NOT_IN_THE_ROOM, lobbyState);
    }

    public enum Reason {
        REQUESTER_IS_NOT_JOINED_TO_GAME,
        GAME_IS_ALREADY_RUNNING,
        REQUESTER_IS_NOT_ADMIN,
        USER_IS_NOT_IN_THE_ROOM
    }
}