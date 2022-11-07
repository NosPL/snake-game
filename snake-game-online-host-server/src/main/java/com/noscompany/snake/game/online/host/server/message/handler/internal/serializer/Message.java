package com.noscompany.snake.game.online.host.server.message.handler.internal.serializer;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;

import java.util.function.Consumer;

public interface Message {

    void onRoomEnter(Consumer<String> consumer);

    void onTakeASeat(Consumer<PlayerNumber> consumer);

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