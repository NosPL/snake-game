package com.noscompany.snake.game.online.online.contract.serialization;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import io.vavr.control.Option;

public interface ObjectTypeMapper {


    Option<Class<?>> mapToObjectType(OnlineMessage.MessageType messageType);
}
