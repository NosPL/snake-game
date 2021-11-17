package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Walls;

import static com.noscompany.snake.game.commons.MessageDto.MessageType.FAILED_TO_CHANGE_GAME_OPTIONS;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class FailedToChangeGameOptions implements MessageDto {
    MessageDto.MessageType messageType = FAILED_TO_CHANGE_GAME_OPTIONS;
    Reason reason;
    GridSize oldGridSizeOption;
    GameSpeed oldGameSpeedOption;
    Walls oldWallsOption;

    public static FailedToChangeGameOptions requesterIsNotAdmin(GridSize oldGridSizeOption,
                                                                GameSpeed oldGameSpeedOption,
                                                                Walls oldWallsOption) {
        return new FailedToChangeGameOptions(Reason.REQUESTER_IS_NOT_ADMIN,
                oldGridSizeOption,
                oldGameSpeedOption,
                oldWallsOption);
    }

    public static FailedToChangeGameOptions gameIsAlreadyRunning(GridSize oldGridSizeOption,
                                                                 GameSpeed oldGameSpeedOption,
                                                                 Walls oldWallsOption) {
        return new FailedToChangeGameOptions(Reason.GAME_IS_ALREADY_RUNNING,
                oldGridSizeOption,
                oldGameSpeedOption,
                oldWallsOption);
    }

    public static FailedToChangeGameOptions requesterDidNotTookASeat(GridSize oldGridSizeOption,
                                                                     GameSpeed oldGameSpeedOption,
                                                                     Walls oldWallsOption) {
        return new FailedToChangeGameOptions(Reason.REQUESTER_DID_NOT_TOOK_A_SEAT,
                oldGridSizeOption,
                oldGameSpeedOption,
                oldWallsOption);
    }

    public enum Reason {
        REQUESTER_IS_NOT_ADMIN, GAME_IS_ALREADY_RUNNING, REQUESTER_DID_NOT_TOOK_A_SEAT;
    }
}