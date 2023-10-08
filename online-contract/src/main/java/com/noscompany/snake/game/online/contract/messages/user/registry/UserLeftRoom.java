package com.noscompany.snake.game.online.contract.messages.user.registry;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class UserLeftRoom implements OnlineMessage {
    MessageType messageType = MessageType.USER_LEFT_ROOM;
    UserId userId;
    UserName userName;
    Set<UserName> usersInTheRoom;
}