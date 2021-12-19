package snake.game.core.events;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.Getter;

import static com.noscompany.snake.game.commons.OnlineMessage.MessageType.GAME_RESUMED;

@Getter
public class GameResumed implements GameEvent, OnlineMessage {
    OnlineMessage.MessageType messageType = GAME_RESUMED;
}