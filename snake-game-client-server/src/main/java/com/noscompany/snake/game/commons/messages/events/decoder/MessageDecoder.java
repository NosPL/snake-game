package com.noscompany.snake.game.commons.messages.events.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.noscompany.snake.game.commons.MessageDto;
import com.noscompany.snake.game.commons.messages.events.lobby.*;
import io.vavr.control.Option;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import snake.game.core.events.*;

import java.util.function.Consumer;

import static com.noscompany.snake.game.commons.MessageDto.MessageType.*;

@Slf4j
public class MessageDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void onGameCancelled(String json, Consumer<GameCancelled> consumer) {
        messageType(json)
                .filter(messageType -> messageType == GAME_CANCELLED)
                .map(msgType -> getGameCancelled(json, GameCancelled.class))
                .peek(consumer::accept);
    }

    public void onGameContinues(String json, Consumer<GameContinues> consumer) {
        messageType(json)
                .filter(messageType -> messageType == GAME_CONTINUES)
                .map(msgType -> getGameCancelled(json, GameContinues.class))
                .peek(consumer::accept);
    }

    public void onGameFinished(String json, Consumer<GameFinished> consumer) {
        messageType(json)
                .filter(messageType -> messageType == GAME_FINISHED)
                .map(msgType -> getGameCancelled(json, GameFinished.class))
                .peek(consumer::accept);
    }

    public void onGamePaused(String json, Consumer<GamePaused> consumer) {
        messageType(json)
                .filter(messageType -> messageType == GAME_PAUSED)
                .map(msgType -> getGameCancelled(json, GamePaused.class))
                .peek(consumer::accept);
    }

    public void onGameResumed(String json, Consumer<GameResumed> consumer) {
        messageType(json)
                .filter(messageType -> messageType == GAME_RESUMED)
                .map(msgType -> getGameCancelled(json, GameResumed.class))
                .peek(consumer::accept);
    }

    public void onGameStarted(String json, Consumer<GameStarted> consumer) {
        messageType(json)
                .filter(messageType -> messageType == GAME_STARTED)
                .map(msgType -> getGameCancelled(json, GameStarted.class))
                .peek(consumer::accept);
    }

    public void onTimeLeftToGameStartHasChanged(String json, Consumer<TimeLeftToGameStartHasChanged> consumer) {
        messageType(json)
                .filter(messageType -> messageType == TIME_LEFT_TO_GAME_START_CHANGED)
                .map(msgType -> getGameCancelled(json, TimeLeftToGameStartHasChanged.class))
                .peek(consumer::accept);
    }

    public void onChatMessageReceived(String json, Consumer<ChatMessageReceived> consumer) {
        messageType(json)
                .filter(messageType -> messageType == CHAT_MESSAGE_RECEIVED)
                .map(msgType -> getGameCancelled(json, ChatMessageReceived.class))
                .peek(consumer::accept);
    }

    public void onFailedToChangeGameOptions(String json, Consumer<FailedToChangeGameOptions> consumer) {
        messageType(json)
                .filter(messageType -> messageType == FAILED_TO_CHANGE_GAME_OPTIONS)
                .map(msgType -> getGameCancelled(json, FailedToChangeGameOptions.class))
                .peek(consumer::accept);
    }

    public void onFailedToJoinGame(String json, Consumer<FailedToTakeASeat> consumer) {
        messageType(json)
                .filter(messageType -> messageType == FAILED_TO_TAKE_A_SEAT)
                .map(msgType -> getGameCancelled(json, FailedToTakeASeat.class))
                .peek(consumer::accept);
    }

    public void onFailedStartGame(String json, Consumer<FailedToStartGame> consumer) {
        messageType(json)
                .filter(messageType -> messageType == FAILED_TO_START_GAME)
                .map(msgType -> getGameCancelled(json, FailedToStartGame.class))
                .peek(consumer::accept);
    }

    public void onGameOptionsChanged(String json, Consumer<GameOptionsChanged> consumer) {
        messageType(json)
                .filter(messageType -> messageType == GAME_OPTIONS_CHANGED)
                .map(msgType -> getGameCancelled(json, GameOptionsChanged.class))
                .peek(consumer::accept);
    }

    public void onNewUserConnected(String json, Consumer<NewUserAdded> consumer) {
        messageType(json)
                .filter(messageType -> messageType == NEW_USER_ADDED)
                .map(msgType -> getGameCancelled(json, NewUserAdded.class))
                .peek(consumer::accept);
    }

    public void onNewUserConnectedAsAdmin(String json, Consumer<NewUserConnectedAsAdmin> consumer) {
        messageType(json)
                .filter(messageType -> messageType == NEW_USER_ADDED_AS_ADMIN)
                .map(msgType -> getGameCancelled(json, NewUserConnectedAsAdmin.class))
                .peek(consumer::accept);
    }

    public void onPlayerFreedUpASeat(String json, Consumer<PlayerFreedUpASeat> consumer) {
        messageType(json)
                .filter(messageType -> messageType == PLAYER_FREED_UP_SEAT)
                .map(msgType -> getGameCancelled(json, PlayerFreedUpASeat.class))
                .peek(consumer::accept);
    }

    public void onPlayerTookASeat(String json, Consumer<PlayerTookASeat> consumer) {
        messageType(json)
                .filter(messageType -> messageType == PLAYER_TOOK_A_SEAT)
                .map(msgType -> getGameCancelled(json, PlayerTookASeat.class))
                .peek(consumer::accept);
    }

    public void onUserDisconnected(String json, Consumer<UserRemoved> consumer) {
        messageType(json)
                .filter(messageType -> messageType == USER_REMOVED)
                .map(msgType -> getGameCancelled(json, UserRemoved.class))
                .peek(consumer::accept);
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

    @SneakyThrows
    private <T> T getGameCancelled(String json, Class<T> aClass) {
        return objectMapper.readValue(json, aClass);
    }
}