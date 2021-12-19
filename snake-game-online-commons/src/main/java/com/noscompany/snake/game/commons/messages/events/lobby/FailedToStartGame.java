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
public class FailedToStartGame implements OnlineMessage {
    MessageType messageType = MessageType.FAILED_TO_START_GAME;
    Reason reason;
    GameLobbyState lobbyState;

    public static FailedToStartGame requesterDidNotTookASeat(GameLobbyState lobbyState) {
        return new FailedToStartGame(Reason.REQUESTER_IS_NOT_JOINED_TO_GAME, lobbyState);
    }

    public static FailedToStartGame gameIsAlreadyRunning(GameLobbyState lobbyState) {
        return new FailedToStartGame(Reason.GAME_IS_ALREADY_RUNNING, lobbyState);
    }

    public static FailedToStartGame requesterIsNotAdmin(GameLobbyState lobbyState) {
        return new FailedToStartGame(Reason.REQUESTER_IS_NOT_ADMIN, lobbyState);
    }

    public enum Reason {
        REQUESTER_IS_NOT_JOINED_TO_GAME,
        GAME_IS_ALREADY_RUNNING,
        REQUESTER_IS_NOT_ADMIN
    }
}