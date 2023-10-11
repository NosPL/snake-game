package com.noscompany.snake.game.online.online.contract.serialization.object.type.mappers;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.playground.GameReinitialized;
import com.noscompany.snake.game.online.contract.messages.playground.InitializePlaygroundToRemoteClient;
import com.noscompany.snake.game.online.online.contract.serialization.ObjectTypeMapper;
import io.vavr.control.Option;

public final class PlaygroundMessageTypeMapper implements ObjectTypeMapper {

    @Override
    public Option<Class<?>> mapToObjectType(OnlineMessage.MessageType messageType) {
        return switch (messageType) {
            case GAME_REINITIALIZED -> Option.of(GameReinitialized.class);
            case INITIALIZE_PLAYGROUND_STATE -> Option.of(InitializePlaygroundToRemoteClient.class);
            default -> Option.none();
        };
    }
}