package com.noscompany.snake.game.commons.messages.events.game;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.events.GameResumed;

@Value
@NoArgsConstructor(force = true)
public class GameResumedDto implements MessageDto {
    MessageType messageType = MessageType.GAME_RESUMED;

    public GameResumed toGameEvent() {
        return new GameResumed();
    }

    public static GameResumedDto dtoFrom(GameResumed event) {
        return new GameResumedDto();
    }
}