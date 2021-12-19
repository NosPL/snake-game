package com.noscompany.snake.game.commons.messages.commands.lobby;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true)
public class FreeUpASeat implements OnlineMessage {
    MessageType messageType = MessageType.FREE_UP_A_SEAT;
}
