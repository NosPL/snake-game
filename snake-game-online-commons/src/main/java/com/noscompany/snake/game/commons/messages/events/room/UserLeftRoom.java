package com.noscompany.snake.game.commons.messages.events.room;

import com.noscompany.snake.game.commons.OnlineMessage;
import com.noscompany.snake.game.commons.messages.events.lobby.PlayerFreedUpASeat;
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
    String userName;
    Set<String> usersList;
    Option<PlayerFreedUpASeat> playerFreedUpASeat;
}