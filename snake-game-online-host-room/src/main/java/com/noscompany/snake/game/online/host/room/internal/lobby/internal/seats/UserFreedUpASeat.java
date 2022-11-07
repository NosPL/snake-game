package com.noscompany.snake.game.online.host.room.internal.lobby.internal.seats;

import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import lombok.Value;

@Value
public class UserFreedUpASeat {
    String userName;
    PlayerNumber freedUpSeatNumber;
}