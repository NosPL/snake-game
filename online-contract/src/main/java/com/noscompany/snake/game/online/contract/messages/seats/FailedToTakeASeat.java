package com.noscompany.snake.game.online.contract.messages.seats;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
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

    public enum Reason {
        USER_NOT_IN_THE_ROOM,
        GAME_ALREADY_RUNNING,
        SEAT_ALREADY_TAKEN
    }

    public static FailedToTakeASeat userNotInTheRoom() {
        return new FailedToTakeASeat(Reason.USER_NOT_IN_THE_ROOM);
    }

    public static FailedToTakeASeat gameAlreadyRunning() {
        return new FailedToTakeASeat(Reason.GAME_ALREADY_RUNNING);
    }

    public static FailedToTakeASeat seatAlreadyTaken() {
        return new FailedToTakeASeat(Reason.SEAT_ALREADY_TAKEN);
    }
}