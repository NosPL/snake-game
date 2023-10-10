package com.noscompany.snake.game.online.online.contract.serialization;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.EnterRoom;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

@AllArgsConstructor
final class OnlineContractObjectTypeMapper implements ObjectTypeMapper {

    @Override
    public Option<Class<?>> mapToObjectType(OnlineMessage.MessageType messageType) {
        return switch (messageType) {
            case CHANGE_SNAKE_DIRECTION -> Option.of(ChangeSnakeDirection.class);
            case TAKE_A_SEAT -> Option.of(TakeASeat.class);
            case CHANGE_GAME_OPTIONS -> Option.of(ChangeGameOptions.class);
            case FREE_UP_A_SEAT -> Option.of(FreeUpASeat.class);
            case START_GAME -> Option.of(StartGame.class);
            case CANCEL_GAME -> Option.of(CancelGame.class);
            case PAUSE_GAME -> Option.of(PauseGame.class);
            case RESUME_GAME -> Option.of(ResumeGame.class);
            case ENTER_THE_ROOM -> Option.of(EnterRoom.class);
            default -> Option.none();
        };
    }
}