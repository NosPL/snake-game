package snake.game.core.events;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.Getter;

import static com.noscompany.snake.game.commons.MessageDto.MessageType.GAME_RESUMED;

@Getter
public class GameResumed implements GameEvent, MessageDto {
    MessageDto.MessageType messageType = GAME_RESUMED;
}