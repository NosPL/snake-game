package com.noscompany.snake.game.online.online.contract.serialization.object.type.mappers;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.*;
import com.noscompany.snake.game.online.online.contract.serialization.ObjectTypeMapper;
import io.vavr.control.Option;

public final class GameOptionsTypeMapper implements ObjectTypeMapper {

    @Override
    public Option<Class<?>> mapToObjectType(OnlineMessage.MessageType messageType) {
        return switch (messageType) {
            case INITIALIZE_GAME_OPTIONS -> Option.of(InitializeGameOptions.class);
            case CHANGE_GAME_OPTIONS -> Option.of(ChangeGameOptions.class);
            case GAME_OPTIONS_CHANGED -> Option.of(GameOptionsChanged.class);
            case FAILED_TO_CHANGE_GAME_OPTIONS -> Option.of(FailedToChangeGameOptions.class);
            default -> Option.none();
        };
    }
}