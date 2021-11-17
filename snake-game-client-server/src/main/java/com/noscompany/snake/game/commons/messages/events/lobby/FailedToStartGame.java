package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static com.noscompany.snake.game.commons.messages.events.lobby.FailedToStartGame.Reason.*;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class FailedToStartGame implements MessageDto {
    MessageType messageType = MessageType.FAILED_TO_START_GAME;
    Reason reason;

    public static FailedToStartGame requesterDidNotTookASeat() {
        return new FailedToStartGame(REQUESTER_IS_NOT_JOINED_TO_GAME);
    }

    public static FailedToStartGame gameIsAlreadyRunning() {
        return new FailedToStartGame(GAME_IS_ALREADY_RUNNING);
    }

    public static FailedToStartGame requesterIsNotAdmin() {
        return new FailedToStartGame(REQUESTER_IS_NOT_ADMIN);
    }

    public enum Reason {
        REQUESTER_IS_NOT_JOINED_TO_GAME,
        GAME_IS_ALREADY_RUNNING,
        REQUESTER_IS_NOT_ADMIN
    }
}