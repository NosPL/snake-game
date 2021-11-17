package snake.game.core.events;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.Getter;

import static com.noscompany.snake.game.commons.MessageDto.MessageType.GAME_PAUSED;

@Getter
public class GamePaused implements GameEvent, MessageDto {
    MessageDto.MessageType messageType = GAME_PAUSED;
}
