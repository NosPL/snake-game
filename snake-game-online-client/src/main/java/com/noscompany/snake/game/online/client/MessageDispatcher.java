package com.noscompany.snake.game.online.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.noscompany.snake.game.commons.OnlineMessage.MessageType;
import com.noscompany.snake.game.commons.messages.events.chat.ChatMessageReceived;
import com.noscompany.snake.game.commons.messages.events.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.commons.messages.events.lobby.*;
import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import com.noscompany.snake.game.commons.messages.events.room.NewUserEnteredRoom;
import com.noscompany.snake.game.commons.messages.events.room.UserLeftRoom;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.Function;
import snake.game.core.events.*;

import static com.noscompany.snake.game.commons.OnlineMessage.MessageType.*;

@AllArgsConstructor
@Slf4j
class
MessageDispatcher implements Function<String> {
    private final ClientEventHandler eventHandler;
    private final ObjectMapper objectMapper;

    @Override
    public void on(String json) {
        getTypeOf(json)
                .peek(messageType -> handle(json, messageType));
    }

    @SneakyThrows
    private void handle(String json, MessageType messageType) {
        if (messageType == NEW_USER_ENTERED_ROOM) {
            var event = objectMapper.readValue(json, NewUserEnteredRoom.class);
            eventHandler.handle(event);
        } else if (messageType == FAILED_TO_ENTER_THE_ROOM) {
            var event = objectMapper.readValue(json, FailedToEnterRoom.class);
            eventHandler.handle(event);
        } else if (messageType == GAME_OPTIONS_CHANGED) {
            var event = objectMapper.readValue(json, GameOptionsChanged.class);
            eventHandler.handle(event);
        } else if (messageType == FAILED_TO_CHANGE_GAME_OPTIONS) {
            var event = objectMapper.readValue(json, FailedToChangeGameOptions.class);
            eventHandler.handle(event);
        } else if (messageType == PLAYER_TOOK_A_SEAT) {
            var event = objectMapper.readValue(json, PlayerTookASeat.class);
            eventHandler.handle(event);
        } else if (messageType == FAILED_TO_TAKE_A_SEAT) {
            var event = objectMapper.readValue(json, FailedToTakeASeat.class);
            eventHandler.handle(event);
        } else if (messageType == PLAYER_FREED_UP_SEAT) {
            var event = objectMapper.readValue(json, PlayerFreedUpASeat.class);
            eventHandler.handle(event);
        } else if (messageType == FAILED_TO_FREE_UP_A_SEAT) {
            var event = objectMapper.readValue(json, FailedToFreeUpSeat.class);
            eventHandler.handle(event);
        } else if (messageType == CHAT_MESSAGE_RECEIVED) {
            var event = objectMapper.readValue(json, ChatMessageReceived.class);
            eventHandler.handle(event);
        } else if (messageType == FAILED_TO_SEND_CHAT_MESSAGE) {
            var event = objectMapper.readValue(json, FailedToSendChatMessage.class);
            eventHandler.handle(event);
        } else if (messageType == USER_LEFT_ROOM) {
            var event = objectMapper.readValue(json, UserLeftRoom.class);
            eventHandler.handle(event);
        } else if (messageType == FAILED_TO_START_GAME) {
            var event = objectMapper.readValue(json, FailedToStartGame.class);
            eventHandler.handle(event);
        } else if (messageType == TIME_LEFT_TO_GAME_START_CHANGED) {
            var event = objectMapper.readValue(json, TimeLeftToGameStartHasChanged.class);
            eventHandler.handle(event);
        } else if (messageType == GAME_STARTED) {
            var event = objectMapper.readValue(json, GameStarted.class);
            eventHandler.handle(event);
        } else if (messageType == GAME_CONTINUES) {
            var event = objectMapper.readValue(json, GameContinues.class);
            eventHandler.handle(event);
        } else if (messageType == GAME_FINISHED) {
            var event = objectMapper.readValue(json, GameFinished.class);
            eventHandler.handle(event);
        } else if (messageType == GAME_CANCELLED) {
            var event = objectMapper.readValue(json, GameCancelled.class);
            eventHandler.handle(event);
        } else if (messageType == GAME_PAUSED) {
            var event = objectMapper.readValue(json, GamePaused.class);
            eventHandler.handle(event);
        } else if (messageType == GAME_RESUMED) {
            var event = objectMapper.readValue(json, GameResumed.class);
            eventHandler.handle(event);
        }
    }

    private Option<MessageType> getTypeOf(String msg) {
        try {
            String msgType = JsonPath
                    .parse(msg)
                    .read("$.messageType");
            var messageType = MessageType.valueOf(msgType);
            return Option.of(messageType);
        } catch (Exception e) {
            log.warn("failed to parse message type", e);
            return Option.none();
        }
    }
}