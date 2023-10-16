package com.noscompany.snake.game.online.contract.messages.seats;

import com.noscompany.snake.game.online.contract.messages.DedicatedClientMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Set;

import static com.noscompany.snake.game.online.contract.messages.OnlineMessage.MessageType.INITIALIZE_SEATS_TO_REMOTE_CLIENT;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class InitializeSeats implements DedicatedClientMessage {
    MessageType messageType = INITIALIZE_SEATS_TO_REMOTE_CLIENT;
    UserId userId;
    Option<AdminId> adminIdOption;
    Set<Seat> seats;
}