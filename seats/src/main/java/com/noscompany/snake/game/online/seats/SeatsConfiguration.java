package com.noscompany.snake.game.online.seats;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.CountdownTime;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameCancelled;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameFinished;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStarted;
import com.noscompany.snake.game.online.contract.messages.host.HostGotShutdown;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SeatsConfiguration {

    public Seats create(MessagePublisher messagePublisher) {
        var seats = new Seats(new HashMap<>(), seatsByPlayerNumber(), Option.none(), new AtomicBoolean(false));
        Subscription subscription = createSubscription(seats);
        messagePublisher.subscribe(subscription);
        return seats;
    }

    private Map<PlayerNumber, Seat> seatsByPlayerNumber() {
        return Vector
                .of(PlayerNumber.values())
                .map(Seat::create)
                .toMap(Seat::getPlayerNumber, seat -> seat)
                .toJavaMap();
    }

    private Subscription createSubscription(Seats seats) {
        return new Subscription()
//                user registry events
                .toMessage(NewUserEnteredRoom.class, (NewUserEnteredRoom msg) -> seats.newUserEnteredRoom(msg.getUserId(), msg.getUserName()))
                .toMessage(UserLeftRoom.class, (UserLeftRoom msg) -> seats.userLeftRoom(msg.getUserId()))
//                seats commands
                .toMessage(FreeUpASeat.class, (FreeUpASeat msg) -> seats.freeUpSeat(msg.getUserId()))
                .toMessage(TakeASeat.class, (TakeASeat msg) -> seats.takeOrChangeSeat(msg.getUserId(), msg.getPlayerNumber()))
//                gameplay events
                .toMessage(CountdownTime.class, (CountdownTime msg) -> seats.gameStarted())
                .toMessage(GameStarted.class, (GameStarted msg) -> seats.gameFinished())
                .toMessage(GameCancelled.class, (GameCancelled msg) -> seats.gameFinished())
                .toMessage(GameFinished.class, (GameFinished msg) -> seats.gameFinished())
//                host event
                .toMessage(HostGotShutdown.class, (HostGotShutdown msg) -> seats.terminate());
    }
}