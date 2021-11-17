package snake.game.core.events;

import com.noscompany.snake.game.commons.MessageDto;

import static com.noscompany.snake.game.commons.MessageDto.MessageType.GAME_PAUSED;

public class GamePaused implements GameEvent {
    MessageDto.MessageType messageType = GAME_PAUSED;
}
