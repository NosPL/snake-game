package com.noscompany.snake.game.online.contract.messages.lobby.command;

import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class TakeASeat implements OnlineMessage {
    MessageType messageType = MessageType.TAKE_A_SEAT;
    PlayerNumber playerNumber;
}