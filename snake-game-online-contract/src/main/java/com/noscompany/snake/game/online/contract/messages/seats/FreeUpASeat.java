package com.noscompany.snake.game.online.contract.messages.seats;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class FreeUpASeat implements OnlineMessage {
    MessageType messageType = MessageType.FREE_UP_A_SEAT;
}