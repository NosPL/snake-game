package chat.messages;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.ObjectTypeMapper;
import io.vavr.control.Option;

public final class ChatMessageTypeMapper implements ObjectTypeMapper {

    @Override
    public Option<Class<?>> mapToObjectType(OnlineMessage.MessageType messageType) {
        return switch (messageType) {
            case SEND_CHAT_MESSAGE -> Option.of(SendChatMessage.class);
            case USER_SENT_CHAT_MESSAGE -> Option.of(UserSentChatMessage.class);
            case FAILED_TO_SEND_CHAT_MESSAGE -> Option.of(FailedToSendChatMessage.class);
            default -> Option.none();
        };
    }
}