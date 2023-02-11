package com.noscompany.snake.game.online.host.room.internal.playground;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import io.vavr.collection.Vector;

import java.util.Map;

class SeatsCreator {

    public static Seats create() {
        Map<PlayerNumber, Seat> seats = Vector
                .of(PlayerNumber.values())
                .map(Seat::create)
                .toMap(Seat::getPlayerNumber, seat -> seat)
                .toJavaMap();
        return new Seats(seats);
    }
}