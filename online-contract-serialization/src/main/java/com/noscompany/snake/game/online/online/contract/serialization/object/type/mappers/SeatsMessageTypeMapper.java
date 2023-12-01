package com.noscompany.snake.game.online.online.contract.serialization.object.type.mappers;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.seats.*;
import com.noscompany.snake.game.online.contract.messages.ObjectTypeMapper;
import io.vavr.control.Option;

public final class SeatsMessageTypeMapper implements ObjectTypeMapper {

    @Override
    public Option<Class<?>> mapToObjectType(OnlineMessage.MessageType messageType) {
        return switch (messageType) {
            case TAKE_A_SEAT -> Option.of(TakeASeat.class);
            case FREE_UP_A_SEAT -> Option.of(FreeUpASeat.class);

            case PLAYER_TOOK_A_SEAT -> Option.of(PlayerTookASeat.class);
            case PLAYER_FREED_UP_SEAT -> Option.of(PlayerFreedUpASeat.class);

            case FAILED_TO_TAKE_A_SEAT -> Option.of(FailedToTakeASeat.class);
            case FAILED_TO_FREE_UP_A_SEAT -> Option.of(FailedToFreeUpSeat.class);

            case INITIALIZE_SEATS_TO_REMOTE_CLIENT -> Option.of(InitializeSeats.class);
            default -> Option.none();
        };
    }
}