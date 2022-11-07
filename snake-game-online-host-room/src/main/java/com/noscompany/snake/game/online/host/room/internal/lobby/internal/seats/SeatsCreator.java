package com.noscompany.snake.game.online.host.room.internal.lobby.internal.seats;

import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import io.vavr.collection.Vector;

import java.util.Map;

public class SeatsCreator {

    public static SeatsImpl create() {
        Map<PlayerNumber, Seat> seats = Vector
                .of(PlayerNumber.values())
                .map(Seat::create)
                .toMap(Seat::getPlayerNumber, seat -> seat)
                .toJavaMap();
        return new SeatsImpl(seats);
    }
}