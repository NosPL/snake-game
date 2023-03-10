package com.noscompany.snake.game.online.contract.messages.gameplay.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToResumeGame implements OnlineMessage {
    OnlineMessage.MessageType messageType = MessageType.FAILED_TO_RESUME_GAME;
    Reason reason;

    public static FailedToResumeGame userNotInTheRoom() {
        return new FailedToResumeGame(Reason.USER_NOT_IN_THE_ROOM);
    }

    public static FailedToResumeGame gameNotStarted() {
        return new FailedToResumeGame(Reason.GAME_NOT_STARTED);
    }

    public static FailedToResumeGame playerDidNotTakeASeat() {
        return new FailedToResumeGame(Reason.PLAYER_DID_NOT_TAKE_SEAT);
    }

    public static FailedToResumeGame playerIsNotAdmin() {
        return new FailedToResumeGame(Reason.PLAYER_IS_NOT_ADMIN);
    }

    public enum Reason {
        USER_NOT_IN_THE_ROOM,
        GAME_NOT_STARTED,
        PLAYER_DID_NOT_TAKE_SEAT,
        PLAYER_IS_NOT_ADMIN
    }
}