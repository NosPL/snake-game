package com.noscompany.snake.game.online.contract.messages.gameplay.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
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

    public static FailedToStartGame requesterDidNotTakeASeat() {
        return new FailedToStartGame(Reason.REQUESTER_DID_NOT_TAKE_A_SEAT);
    }

    public static FailedToStartGame gameIsAlreadyRunning() {
        return new FailedToStartGame(Reason.GAME_IS_ALREADY_RUNNING);
    }

    public static FailedToStartGame requesterIsNotAdmin() {
        return new FailedToStartGame(Reason.REQUESTER_IS_NOT_ADMIN);
    }

    public static FailedToStartGame userIsNotInTheRoom() {
        return new FailedToStartGame(Reason.USER_IS_NOT_IN_THE_ROOM);
    }

    public enum Reason {
        REQUESTER_DID_NOT_TAKE_A_SEAT,
        GAME_IS_ALREADY_RUNNING,
        REQUESTER_IS_NOT_ADMIN,
        USER_IS_NOT_IN_THE_ROOM
    }
}