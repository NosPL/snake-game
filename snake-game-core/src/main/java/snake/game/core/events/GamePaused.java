package snake.game.core.events;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.Getter;

import static com.noscompany.snake.game.commons.OnlineMessage.MessageType.GAME_PAUSED;

@Getter
public class GamePaused implements GameEvent, OnlineMessage {
    OnlineMessage.MessageType messageType = GAME_PAUSED;
}
