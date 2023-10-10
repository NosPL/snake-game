package com.noscompany.snake.game.online.contract.messages.user.registry;

import com.noscompany.snake.game.online.contract.messages.PublicClientMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class NewUserEnteredRoom implements PublicClientMessage {
    MessageType messageType = MessageType.NEW_USER_ENTERED_ROOM;
    UserId userId;
    UserName userName;
    Set<UserName> usersInTheRoom;
}