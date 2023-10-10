package com.noscompany.snake.game.online.online.contract.serialization.object.type.mappers;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.online.contract.serialization.ObjectTypeMapper;
import io.vavr.control.Option;

public final class GameplayTypeMapper implements ObjectTypeMapper {

    @Override
    public Option<Class<?>> mapToObjectType(OnlineMessage.MessageType messageType) {
        return switch (messageType) {
            case CANCEL_GAME -> Option.of(CancelGame.class);
            case CHANGE_SNAKE_DIRECTION -> Option.of(ChangeSnakeDirection.class);
            case PAUSE_GAME -> Option.of(PauseGame.class);
            case RESUME_GAME -> Option.of(ResumeGame.class);
            case START_GAME -> Option.of(StartGame.class);

            case FAILED_TO_CANCEL_GAME -> Option.of(FailedToCancelGame.class);
            case FAILED_TO_CHANGE_DIRECTION -> Option.of(FailedToChangeSnakeDirection.class);
            case FAILED_TO_PAUSE_GAME -> Option.of(FailedToPauseGame.class);
            case FAILED_TO_RESUME_GAME -> Option.of(FailedToResumeGame.class);
            case FAILED_TO_START_GAME -> Option.of(FailedToStartGame.class);

            case GAME_CANCELLED -> Option.of(GameCancelled.class);
            case GAME_FINISHED -> Option.of(GameFinished.class);
            case GAME_PAUSED -> Option.of(GamePaused.class);
            case GAME_RESUMED -> Option.of(GameResumed.class);
            case TIME_LEFT_TO_GAME_START_CHANGED -> Option.of(GameStartCountdown.class);
            case GAME_STARTED -> Option.of(GameStarted.class);
            case SNAKES_MOVED -> Option.of(SnakesMoved.class);

            default -> Option.none();
        };
    }
}