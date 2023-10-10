package com.noscompany.snake.game.online.contract.messages.seats;

import com.noscompany.snake.game.online.contract.messages.PublicClientMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class PlayerTookASeat implements PublicClientMessage {
    MessageType messageType = MessageType.PLAYER_TOOK_A_SEAT;
    UserId userId;
    UserName userName;
    PlayerNumber playerNumber;
    AdminId adminId;
    Set<Seat> seats;
}