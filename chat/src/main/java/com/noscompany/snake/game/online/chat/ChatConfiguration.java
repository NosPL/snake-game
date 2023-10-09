package com.noscompany.snake.game.online.chat;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;

import java.util.HashMap;

public final class ChatConfiguration {

    public Chat createChat(MessagePublisher messagePublisher) {
        Chat chat = new Chat(new HashMap<>());
        messagePublisher.subscribe(createSubscription(chat));
        return chat;
    }

    private Subscription createSubscription(Chat chat) {
        return new Subscription()
                .subscriberName("chat")
                .toMessage(SendChatMessage.class, (SendChatMessage command) -> chat.sendMessage(command.getUserId(), command.getMessageContent()))
                .toMessage(NewUserEnteredRoom.class, (NewUserEnteredRoom event) -> chat.newUserEnteredRoom(event.getUserId(), event.getUserName()))
                .toMessage(UserLeftRoom.class, (UserLeftRoom event) -> chat.userLeftRoom(event.getUserId()));
    }
}