package com.noscompany.snake.game.online.online.contract.serialization.object.type.mappers;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.user.registry.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.ObjectTypeMapper;
import io.vavr.control.Option;

public final class UserRegistryMessageTypeMapper implements ObjectTypeMapper {

    @Override
    public Option<Class<?>> mapToObjectType(OnlineMessage.MessageType messageType) {
        return switch (messageType) {
            case ENTER_THE_ROOM -> Option.of(EnterRoom.class);

            case NEW_USER_ENTERED_ROOM -> Option.of(NewUserEnteredRoom.class);
            case FAILED_TO_ENTER_THE_ROOM -> Option.of(FailedToEnterRoom.class);

            case USER_LEFT_ROOM -> Option.of(UserLeftRoom.class);

            default -> Option.none();
        };
    }
}