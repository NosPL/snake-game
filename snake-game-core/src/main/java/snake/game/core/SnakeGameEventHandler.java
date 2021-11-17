package snake.game.core;

import snake.game.core.events.*;

public interface SnakeGameEventHandler {

    void handle(TimeLeftToGameStartHasChanged event);

    void handle(GameStarted event);

    void handle(GameContinues event);

    void handle(GameFinished event);

    void handle(GameCancelled event);

    void handle(GamePaused event);

    void handle(GameResumed event);
}