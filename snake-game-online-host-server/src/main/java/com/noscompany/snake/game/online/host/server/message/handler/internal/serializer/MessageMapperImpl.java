package com.noscompany.snake.game.online.host.server.message.handler.internal.serializer;

import com.jayway.jsonpath.JsonPath;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.noscompany.snake.game.online.contract.messages.OnlineMessage.MessageType.*;

@Slf4j
class MessageMapperImpl implements MessageMapper {

    public Message map(String message) {
        return getTypeOf(message)
                .map(messageType -> toMessage(message, messageType))
                .getOrElse(new UnknownMessage(message));
    }

    private Message toMessage(String message, OnlineMessage.MessageType messageType) {
        if (messageType == CHANGE_SNAKE_DIRECTION)
            return toChangeSnakeDirection(message);
        if (messageType == TAKE_A_SEAT)
            return toTakeASeat(message);
        if (messageType == CHANGE_GAME_OPTIONS)
            return toChangeGameOptions(message);
        if (messageType == SEND_CHAT_MESSAGE)
            return toChatMessage(message);
        if (messageType == FREE_UP_A_SEAT)
            return new FreeUpASeat();
        if (messageType == START_GAME)
            return new StartGame();
        if (messageType == CANCEL_GAME)
            return new CancelGame();
        if (messageType == PAUSE_GAME)
            return new PauseGame();
        if (messageType == RESUME_GAME)
            return new ResumeGame();
        if (messageType == ENTER_THE_ROOM)
            return toEnterTheRoom(message);
        return new UnknownMessage(message);
    }

    private Message toChatMessage(String message) {
        return tryToMessage(
                () -> {
                    String messageContent = JsonPath.parse(message).read("$.messageContent");
                    return new SendChatMessage(messageContent);
                },
                message);
    }

    private Message toEnterTheRoom(String message) {
        return tryToMessage(
                () -> {
                    String userName = JsonPath.parse(message).read("$.userName");
                    return new EnterRoom(userName);
                },
                message);
    }

    private Message toChangeGameOptions(String message) {
        return tryToMessage(
                () -> {
                    String gridSizeStr = JsonPath.parse(message).read("$.gridSize");
                    String gameSpeedStr = JsonPath.parse(message).read("$.gameSpeed");
                    String wallsStr = JsonPath.parse(message).read("$.walls");
                    return new ChangeGameOptions(
                            GridSize.valueOf(gridSizeStr),
                            GameSpeed.valueOf(gameSpeedStr),
                            Walls.valueOf(wallsStr));
                },
                message);
    }

    private Message toChangeSnakeDirection(String message) {
        return tryToMessage(
                () -> {
                    String snakeDirectionStr = JsonPath.parse(message).read("$.direction");
                    return new ChangeSnakeDirection(Direction.valueOf(snakeDirectionStr));
                },
                message);
    }

    private Message toTakeASeat(String message) {
        return tryToMessage(
                () -> {
                    String snakeNumberStr = JsonPath.parse(message).read("$.playerNumber");
                    return new TakeASeat(PlayerNumber.valueOf(snakeNumberStr));
                },
                message);
    }

    private Message tryToMessage(Supplier<Message> supplier, String message) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.warn("failed map message: {}", message, e);
            return new UnknownMessage(message);
        }
    }

    @AllArgsConstructor
    class EnterRoom implements Message {
        String userName;

        @Override
        public void onRoomEnter(Consumer<String> consumer) {
            consumer.accept(userName);
        }

        @Override
        public void onTakeASeat(Consumer<PlayerNumber> consumer) {

        }

        @Override
        public void onFreeUpASeat(Runnable runnable) {

        }

        @Override
        public void onChangeGameOptions(Consumer3<GridSize, GameSpeed, Walls> consumer) {

        }

        @Override
        public void onStartGame(Runnable runnable) {

        }

        @Override
        public void onChangeSnakeDirection(Consumer<Direction> consumer) {

        }

        @Override
        public void onCancelGame(Runnable runnable) {

        }

        @Override
        public void onPauseGame(Runnable runnable) {

        }

        @Override
        public void onResumeGame(Runnable runnable) {

        }

        @Override
        public void onSendChatMessage(Consumer<String> consumer) {

        }

        @Override
        public void onUnknownMessage(Consumer<String> consumer) {

        }
    }

    @AllArgsConstructor
    class TakeASeat implements Message {
        PlayerNumber playerNumber;

        @Override
        public void onRoomEnter(Consumer<String> consumer) {

        }

        @Override
        public void onTakeASeat(Consumer<PlayerNumber> consumer) {
            consumer.accept(playerNumber);
        }

        @Override
        public void onFreeUpASeat(Runnable runnable) {

        }

        @Override
        public void onChangeGameOptions(Consumer3<GridSize, GameSpeed, Walls> consumer) {

        }

        @Override
        public void onStartGame(Runnable runnable) {

        }

        @Override
        public void onChangeSnakeDirection(Consumer<Direction> consumer) {

        }

        @Override
        public void onCancelGame(Runnable runnable) {

        }

        @Override
        public void onPauseGame(Runnable runnable) {

        }

        @Override
        public void onResumeGame(Runnable runnable) {

        }

        @Override
        public void onSendChatMessage(Consumer<String> consumer) {

        }

        @Override
        public void onUnknownMessage(Consumer<String> consumer) {

        }
    }

    class FreeUpASeat implements Message {

        @Override
        public void onRoomEnter(Consumer<String> consumer) {

        }

        @Override
        public void onTakeASeat(Consumer<PlayerNumber> consumer) {

        }

        @Override
        public void onFreeUpASeat(Runnable runnable) {
            runnable.run();
        }

        @Override
        public void onChangeGameOptions(Consumer3<GridSize, GameSpeed, Walls> consumer) {

        }

        @Override
        public void onStartGame(Runnable runnable) {

        }

        @Override
        public void onChangeSnakeDirection(Consumer<Direction> consumer) {

        }

        @Override
        public void onCancelGame(Runnable runnable) {

        }

        @Override
        public void onPauseGame(Runnable runnable) {

        }

        @Override
        public void onResumeGame(Runnable runnable) {

        }

        @Override
        public void onSendChatMessage(Consumer<String> consumer) {

        }

        @Override
        public void onUnknownMessage(Consumer<String> consumer) {

        }
    }

    @AllArgsConstructor
    class ChangeGameOptions implements Message {
        GridSize gridSize;
        GameSpeed gameSpeed;
        Walls walls;

        @Override
        public void onRoomEnter(Consumer<String> consumer) {

        }

        @Override
        public void onTakeASeat(Consumer<PlayerNumber> consumer) {

        }

        @Override
        public void onFreeUpASeat(Runnable runnable) {

        }

        @Override
        public void onChangeGameOptions(Consumer3<GridSize, GameSpeed, Walls> consumer) {
            consumer.accept(gridSize, gameSpeed, walls);
        }

        @Override
        public void onStartGame(Runnable runnable) {

        }

        @Override
        public void onChangeSnakeDirection(Consumer<Direction> consumer) {

        }

        @Override
        public void onCancelGame(Runnable runnable) {

        }

        @Override
        public void onPauseGame(Runnable runnable) {

        }

        @Override
        public void onResumeGame(Runnable runnable) {

        }

        @Override
        public void onSendChatMessage(Consumer<String> consumer) {

        }

        @Override
        public void onUnknownMessage(Consumer<String> consumer) {

        }
    }

    class StartGame implements Message {

        @Override
        public void onRoomEnter(Consumer<String> consumer) {

        }

        @Override
        public void onTakeASeat(Consumer<PlayerNumber> consumer) {

        }

        @Override
        public void onFreeUpASeat(Runnable runnable) {

        }

        @Override
        public void onChangeGameOptions(Consumer3<GridSize, GameSpeed, Walls> consumer) {

        }

        @Override
        public void onStartGame(Runnable runnable) {
            runnable.run();
        }

        @Override
        public void onChangeSnakeDirection(Consumer<Direction> consumer) {

        }

        @Override
        public void onCancelGame(Runnable runnable) {

        }

        @Override
        public void onPauseGame(Runnable runnable) {

        }

        @Override
        public void onResumeGame(Runnable runnable) {

        }

        @Override
        public void onSendChatMessage(Consumer<String> consumer) {

        }

        @Override
        public void onUnknownMessage(Consumer<String> consumer) {

        }
    }

    @AllArgsConstructor
    class ChangeSnakeDirection implements Message {
        Direction direction;

        @Override
        public void onRoomEnter(Consumer<String> consumer) {

        }

        @Override
        public void onTakeASeat(Consumer<PlayerNumber> consumer) {

        }

        @Override
        public void onFreeUpASeat(Runnable runnable) {

        }

        @Override
        public void onChangeGameOptions(Consumer3<GridSize, GameSpeed, Walls> consumer) {

        }

        @Override
        public void onStartGame(Runnable runnable) {

        }

        @Override
        public void onChangeSnakeDirection(Consumer<Direction> consumer) {
            consumer.accept(direction);
        }

        @Override
        public void onCancelGame(Runnable runnable) {

        }

        @Override
        public void onPauseGame(Runnable runnable) {

        }

        @Override
        public void onResumeGame(Runnable runnable) {

        }

        @Override
        public void onSendChatMessage(Consumer<String> consumer) {

        }

        @Override
        public void onUnknownMessage(Consumer<String> consumer) {

        }
    }

    @AllArgsConstructor
    class CancelGame implements Message {

        @Override
        public void onRoomEnter(Consumer<String> consumer) {

        }

        @Override
        public void onTakeASeat(Consumer<PlayerNumber> consumer) {

        }

        @Override
        public void onFreeUpASeat(Runnable runnable) {

        }

        @Override
        public void onChangeGameOptions(Consumer3<GridSize, GameSpeed, Walls> consumer) {

        }

        @Override
        public void onStartGame(Runnable runnable) {

        }

        @Override
        public void onChangeSnakeDirection(Consumer<Direction> consumer) {

        }

        @Override
        public void onCancelGame(Runnable runnable) {
            runnable.run();
        }

        @Override
        public void onPauseGame(Runnable runnable) {

        }

        @Override
        public void onResumeGame(Runnable runnable) {

        }

        @Override
        public void onSendChatMessage(Consumer<String> consumer) {

        }

        @Override
        public void onUnknownMessage(Consumer<String> consumer) {

        }
    }

    @AllArgsConstructor
    class PauseGame implements Message {

        @Override
        public void onRoomEnter(Consumer<String> consumer) {

        }

        @Override
        public void onTakeASeat(Consumer<PlayerNumber> consumer) {

        }

        @Override
        public void onFreeUpASeat(Runnable runnable) {

        }

        @Override
        public void onChangeGameOptions(Consumer3<GridSize, GameSpeed, Walls> consumer) {

        }

        @Override
        public void onStartGame(Runnable runnable) {

        }

        @Override
        public void onChangeSnakeDirection(Consumer<Direction> consumer) {

        }

        @Override
        public void onCancelGame(Runnable runnable) {

        }

        @Override
        public void onPauseGame(Runnable runnable) {
            runnable.run();
        }

        @Override
        public void onResumeGame(Runnable runnable) {

        }

        @Override
        public void onSendChatMessage(Consumer<String> consumer) {

        }

        @Override
        public void onUnknownMessage(Consumer<String> consumer) {

        }
    }

    @AllArgsConstructor
    class ResumeGame implements Message {

        @Override
        public void onRoomEnter(Consumer<String> consumer) {

        }

        @Override
        public void onTakeASeat(Consumer<PlayerNumber> consumer) {

        }

        @Override
        public void onFreeUpASeat(Runnable runnable) {

        }

        @Override
        public void onChangeGameOptions(Consumer3<GridSize, GameSpeed, Walls> consumer) {

        }

        @Override
        public void onStartGame(Runnable runnable) {

        }

        @Override
        public void onChangeSnakeDirection(Consumer<Direction> consumer) {

        }

        @Override
        public void onCancelGame(Runnable runnable) {

        }

        @Override
        public void onPauseGame(Runnable runnable) {

        }

        @Override
        public void onResumeGame(Runnable runnable) {
            runnable.run();
        }

        @Override
        public void onSendChatMessage(Consumer<String> consumer) {

        }

        @Override
        public void onUnknownMessage(Consumer<String> consumer) {

        }
    }

    @AllArgsConstructor
    class SendChatMessage implements Message {
        String chatMessage;

        @Override
        public void onRoomEnter(Consumer<String> consumer) {

        }

        @Override
        public void onTakeASeat(Consumer<PlayerNumber> consumer) {

        }

        @Override
        public void onFreeUpASeat(Runnable runnable) {

        }

        @Override
        public void onChangeGameOptions(Consumer3<GridSize, GameSpeed, Walls> consumer) {

        }

        @Override
        public void onStartGame(Runnable runnable) {

        }

        @Override
        public void onChangeSnakeDirection(Consumer<Direction> consumer) {

        }

        @Override
        public void onCancelGame(Runnable runnable) {

        }

        @Override
        public void onPauseGame(Runnable runnable) {

        }

        @Override
        public void onResumeGame(Runnable runnable) {

        }

        @Override
        public void onSendChatMessage(Consumer<String> consumer) {
            consumer.accept(chatMessage);
        }

        @Override
        public void onUnknownMessage(Consumer<String> consumer) {

        }
    }

    @AllArgsConstructor
    class UnknownMessage implements Message {
        String unknownMessage;

        @Override
        public void onRoomEnter(Consumer<String> consumer) {

        }

        @Override
        public void onTakeASeat(Consumer<PlayerNumber> consumer) {

        }

        @Override
        public void onFreeUpASeat(Runnable runnable) {

        }

        @Override
        public void onChangeGameOptions(Consumer3<GridSize, GameSpeed, Walls> consumer) {

        }

        @Override
        public void onStartGame(Runnable runnable) {

        }

        @Override
        public void onChangeSnakeDirection(Consumer<Direction> consumer) {

        }

        @Override
        public void onCancelGame(Runnable runnable) {

        }

        @Override
        public void onPauseGame(Runnable runnable) {

        }

        @Override
        public void onResumeGame(Runnable runnable) {

        }

        @Override
        public void onSendChatMessage(Consumer<String> consumer) {
        }

        @Override
        public void onUnknownMessage(Consumer<String> consumer) {
            consumer.accept(unknownMessage);
        }
    }
    private Option<OnlineMessage.MessageType> getTypeOf(String msg) {
        try {
            String msgType = JsonPath
                    .parse(msg)
                    .read("$.messageType");
            var messageType = OnlineMessage.MessageType.valueOf(msgType);
            return Option.of(messageType);
        } catch (Exception e) {
            log.warn("failed to parse message type", e);
            return Option.none();
        }
    }
}