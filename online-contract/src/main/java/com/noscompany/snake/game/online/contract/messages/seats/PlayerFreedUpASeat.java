package com.noscompany.snake.game.online.contract.messages.seats;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class PlayerFreedUpASeat implements OnlineMessage {
    MessageType messageType = MessageType.PLAYER_FREED_UP_SEAT;
    UserId userId;
    String userName;
    PlayerNumber freedUpPlayerNumber;
    PlaygroundState playgroundState;
}