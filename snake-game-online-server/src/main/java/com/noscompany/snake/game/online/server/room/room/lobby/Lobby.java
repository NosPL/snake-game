package com.noscompany.snake.game.online.server.room.room.lobby;

import com.noscompany.snake.game.commons.messages.dto.LobbyState;
import com.noscompany.snake.game.commons.messages.events.lobby.*;
import io.vavr.control.Either;
import io.vavr.control.Option;
import snake.game.core.dto.*;

public interface Lobby {

    Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userId, SnakeNumber snakeNumberNumber);

    Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(String userId);

    Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userId,
                                                                            GridSize gridSize,
                                                                            GameSpeed gameSpeed,
                                                                            Walls walls);

    Option<FailedToStartGame> startGame(String userId);

    void changeSnakeDirection(String userId, Direction direction);

    void cancelGame(String userId);

    void pauseGame(String userId);

    void resumeGame(String userId);

    LobbyState getLobbyState();
}