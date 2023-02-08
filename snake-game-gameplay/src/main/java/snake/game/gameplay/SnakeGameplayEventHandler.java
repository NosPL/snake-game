package snake.game.gameplay;

import com.noscompany.snake.game.online.contract.messages.game.events.*;

public interface SnakeGameplayEventHandler {

    void handle(TimeLeftToGameStartHasChanged event);

    void handle(GameStarted event);

    void handle(GameContinues event);

    void handle(GameFinished event);

    void handle(GameCancelled event);

    void handle(GamePaused event);

    void handle(GameResumed event);
}