package com.noscompany.snake.game.commons.messages.events.game;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.events.GamePaused;

@Value
@NoArgsConstructor(force = true)
public class GamePausedDto implements MessageDto {
    MessageType messageType = MessageType.GAME_PAUSED;

    public GamePaused toGameEvent() {
        return new GamePaused();
    }

    public static GamePausedDto dtoFrom(GamePaused event) {
        return new GamePausedDto();
    }
}
