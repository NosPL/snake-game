package com.noscompany.snake.game.online.contract.messages.seats;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import io.vavr.API;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class Seat {
    PlayerNumber playerNumber;
    Option<UserId> userId;
    Option<UserName> userName;
    boolean taken;

    public boolean isAdmin(Option<AdminId> adminIdOption) {
        return API.For(userId, adminIdOption)
                .yield((userId, adminId) -> userId.getId().equals(adminId.getId()))
                .getOrElse(false);
    }
}
