package com.noscompany.snake.game.commons.messages.commands.decoder;

import com.jayway.jsonpath.JsonPath;
import com.noscompany.snake.game.commons.MessageDto;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;
import snake.game.core.dto.*;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
public class CommandDecoder {
    private final ChangeDirectionMapper changeDirectionMapper = new ChangeDirectionMapper();
    private final SendChatMessageMapper chatMessageMapper = new SendChatMessageMapper();
    private final JoinGameMapper joinGameMapper = new JoinGameMapper();
    private final ChangeGameOptionsMapper changeGameOptionsMapper = new ChangeGameOptionsMapper();


    public void onChangeGameOptions(
            AtmosphereResource r,
            String msg,
            Consumer4<String, GridSize, GameSpeed, Walls> consumer4) {
        messageType(msg)
                .filter(messageType -> messageType == MessageDto.MessageType.CHANGE_GAME_OPTIONS)
                .flatMap(messageType -> changeGameOptionsMapper.map(r, msg))
                .peek(t -> consumer4.accept(t._1, t._2, t._3, t._4));
    }

    public void onChangeSnakeDirection(
            AtmosphereResource r,
            String msg,
            BiConsumer<String, Direction> biConsumer) {
        messageType(msg)
                .filter(messageType -> messageType == MessageDto.MessageType.CHANGE_SNAKE_DIRECTION)
                .flatMap(messageType -> changeDirectionMapper.map(r, msg))
                .peek(t -> biConsumer.accept(t._1, t._2));
    }

    public void onStartNewGame(
            AtmosphereResource r,
            String msg,
            Consumer<String> consumer) {
        messageType(msg)
                .filter(messageType -> messageType == MessageDto.MessageType.START_GAME)
                .peek(t -> consumer.accept(r.uuid()));
    }

    public void onTakeASeat(
            AtmosphereResource r,
            String msg,
            BiConsumer<String, SnakeNumber> biConsumer) {
        messageType(msg)
                .filter(messageType -> messageType == MessageDto.MessageType.TAKE_A_SEAT)
                .flatMap(messageType -> joinGameMapper.map(r, msg))
                .peek(t -> biConsumer.accept(t._1, t._2));
    }

    public void onFreeUpASeat(
            AtmosphereResource r,
            String msg,
            Consumer<String> consumer) {
        messageType(msg)
                .filter(messageType -> messageType == MessageDto.MessageType.FREE_UP_A_SEAT)
                .peek(messageType -> consumer.accept(r.uuid()));
    }

    public void onCancelGame(AtmosphereResource user, String stringMessage, Consumer<String> consumer) {
        messageType(stringMessage)
                .filter(messageType -> messageType == MessageDto.MessageType.CANCEL_GAME)
                .peek(mt -> consumer.accept(user.uuid()));
    }

    public void onPauseGame(AtmosphereResource user, String stringMessage, Consumer<String> consumer) {
        messageType(stringMessage)
                .filter(messageType -> messageType == MessageDto.MessageType.PAUSE_GAME)
                .peek(mt -> consumer.accept(user.uuid()));
    }

    public void onResumeGame(AtmosphereResource user, String stringMessage, Consumer<String> consumer) {
        messageType(stringMessage)
                .filter(messageType -> messageType == MessageDto.MessageType.RESUME_GAME)
                .peek(mt -> consumer.accept(user.uuid()));
    }

    public void onSendChatMessage(
            AtmosphereResource r,
            String msg,
            BiConsumer<String, String> consumer) {
        messageType(msg)
                .filter(messageType -> messageType == MessageDto.MessageType.SEND_CHAT_MESSAGE)
                .map(messageType -> chatMessageMapper.map(r, msg))
                .peek(t -> consumer.accept(r.uuid(), msg));
    }

    private Option<MessageDto.MessageType> messageType(String msg) {
        try {
            String msgType = JsonPath
                    .parse(msg)
                    .read("$.messageType");
            var messageType = MessageDto.MessageType.valueOf(msgType);
            return Option.of(messageType);
        } catch (Exception e) {
            log.warn("failed to parse message type", e);
            return Option.none();
        }
    }
}