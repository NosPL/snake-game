package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.OnlineMessage;
import com.noscompany.snake.game.commons.messages.dto.GameLobbyState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static com.noscompany.snake.game.commons.OnlineMessage.MessageType.FAILED_TO_CHANGE_GAME_OPTIONS;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToChangeGameOptions implements OnlineMessage {
    OnlineMessage.MessageType messageType = FAILED_TO_CHANGE_GAME_OPTIONS;
    Reason reason;
    GameLobbyState gameLobbyState;

    public static FailedToChangeGameOptions requesterIsNotAdmin(GameLobbyState lobbyState) {
        return new FailedToChangeGameOptions(Reason.REQUESTER_IS_NOT_ADMIN, lobbyState);
    }

    public static FailedToChangeGameOptions gameIsAlreadyRunning(GameLobbyState lobbyState) {
        return new FailedToChangeGameOptions(Reason.GAME_IS_ALREADY_RUNNING, lobbyState);
    }

    public static FailedToChangeGameOptions requesterDidNotTookASeat(GameLobbyState lobbyState) {
        return new FailedToChangeGameOptions(Reason.REQUESTER_DID_NOT_TOOK_A_SEAT, lobbyState);
    }

    public static FailedToChangeGameOptions userNotInTheRoom(GameLobbyState lobbyState) {
        return new FailedToChangeGameOptions(Reason.USER_IS_NOT_IN_ROOM, lobbyState);
    }

    public enum Reason {
        REQUESTER_IS_NOT_ADMIN,
        GAME_IS_ALREADY_RUNNING,REQUESTER_DID_NOT_TOOK_A_SEAT,
        USER_IS_NOT_IN_ROOM
    }
}