package com.noscompany.snake.game.online.server.room.message.handler;

import com.noscompany.snake.game.online.server.room.message.handler.mapper.Consumer3;
import snake.game.core.dto.*;

import java.util.function.Consumer;

public interface Message {

    void onRoomEnter(Consumer<String> consumer);

    void onTakeASeat(Consumer<SnakeNumber> consumer);

    void onFreeUpASeat(Runnable runnable);

    void onChangeGameOptions(Consumer3<GridSize, GameSpeed, Walls> consumer);

    void onStartGame(Runnable runnable);

    void onChangeSnakeDirection(Consumer<Direction> consumer);

    void onCancelGame(Runnable runnable);

    void onPauseGame(Runnable runnable);

    void onResumeGame(Runnable runnable);

    void onSendChatMessage(Consumer<String> consumer);

    void onUnknownMessage(Consumer<String> consumer);
}