package com.noscompany.snake.game.online.contract.messages.seats;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class PlayerFreedUpASeat implements OnlineMessage {
    MessageType messageType = MessageType.PLAYER_FREED_UP_SEAT;
    UserId userId;
    PlayerNumber freedUpPlayerNumber;
    Option<AdminId> adminId;
    Set<Seat> seats;
}