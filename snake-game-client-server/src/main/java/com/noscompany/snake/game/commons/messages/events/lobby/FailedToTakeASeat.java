package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static com.noscompany.snake.game.commons.messages.events.lobby.FailedToTakeASeat.Reason.*;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class FailedToTakeASeat implements MessageDto {
    MessageType messageType = MessageType.FAILED_TO_TAKE_A_SEAT;
    Reason reason;

    public enum Reason {
        PLAYER_ALREADY_TOOK_A_SEAT,
        GAME_ALREADY_RUNNING,
        SEAT_ALREADY_TAKEN
    }

    public static FailedToTakeASeat playerAlreadyTookASeat() {
        return new FailedToTakeASeat(PLAYER_ALREADY_TOOK_A_SEAT);
    }

    public static FailedToTakeASeat gameAlreadyRunning() {
        return new FailedToTakeASeat(GAME_ALREADY_RUNNING);
    }

    public static FailedToTakeASeat seatAlreadyTaken() {
        return new FailedToTakeASeat(SEAT_ALREADY_TAKEN);
    }
}