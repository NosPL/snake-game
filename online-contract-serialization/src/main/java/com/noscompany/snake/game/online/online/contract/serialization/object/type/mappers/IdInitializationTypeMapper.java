package com.noscompany.snake.game.online.online.contract.serialization.object.type.mappers;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.network.YourIdGotInitialized;
import com.noscompany.snake.game.online.online.contract.serialization.ObjectTypeMapper;
import io.vavr.control.Option;

import static com.noscompany.snake.game.online.contract.messages.OnlineMessage.MessageType.YOUR_ID_GOT_INITIALIZED;

public final class IdInitializationTypeMapper implements ObjectTypeMapper {

    @Override
    public Option<Class<?>> mapToObjectType(OnlineMessage.MessageType messageType) {
        if (messageType == YOUR_ID_GOT_INITIALIZED)
            return Option.of(YourIdGotInitialized.class);
        else
            return Option.none();
    }
}