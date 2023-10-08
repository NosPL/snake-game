package com.noscompany.snake.game.online.client.internal.state.connected;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage.MessageType;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.mediator.InitializeRemoteClientState;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.vavr.control.Try.failure;

@AllArgsConstructor
@Slf4j
class MessageDeserializer {
    private final ObjectMapper objectMapper;

    Try<DeserializedMessage> deserialize(String serializedMessage) {
        return getMessageType(serializedMessage)
                .flatMap(messageType -> mapToObject(serializedMessage, messageType));
    }

    private Try<MessageType> getMessageType(String serializedMessage) {
        return Try.of(() -> {
            String messageTypeName = JsonPath
                    .parse(serializedMessage)
                    .read("$.messageType");
            return MessageType.valueOf(messageTypeName);
        });
    }

    private Try<DeserializedMessage> mapToObject(String serializedMessage, MessageType messageType) {
        return switch (messageType) {
            case USER_CONNECTED_TO_THE_SERVER -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, InitializeRemoteClientState.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case NEW_USER_ENTERED_ROOM -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, NewUserEnteredRoom.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case FAILED_TO_ENTER_THE_ROOM -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, FailedToEnterRoom.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case GAME_OPTIONS_CHANGED -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, GameOptionsChanged.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case FAILED_TO_CHANGE_GAME_OPTIONS -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, FailedToChangeGameOptions.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case PLAYER_TOOK_A_SEAT -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, PlayerTookASeat.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case FAILED_TO_TAKE_A_SEAT -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, FailedToTakeASeat.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case PLAYER_FREED_UP_SEAT -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, PlayerFreedUpASeat.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case FAILED_TO_FREE_UP_A_SEAT -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, FailedToFreeUpSeat.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case USER_SENT_CHAT_MESSAGE -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, UserSentChatMessage.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case FAILED_TO_SEND_CHAT_MESSAGE -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, FailedToSendChatMessage.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case USER_LEFT_ROOM -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, UserLeftRoom.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case FAILED_TO_START_GAME -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, FailedToStartGame.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case TIME_LEFT_TO_GAME_START_CHANGED -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, GameStartCountdown.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case GAME_STARTED -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, GameStarted.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case SNAKES_MOVED -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, SnakesMoved.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case GAME_FINISHED -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, GameFinished.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case GAME_CANCELLED -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, GameCancelled.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case GAME_PAUSED -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, GamePaused.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            case GAME_RESUMED -> Try
                    .of(() -> objectMapper.readValue(serializedMessage, GameResumed.class))
                    .map(event -> new DeserializedMessage(eventHandler -> eventHandler.handle(event)));
            default -> failure(new NotSupportedMessageTypeException(messageType));
        };
    }

    public static class NotSupportedMessageTypeException extends RuntimeException {
        private static final String MESSAGE = "failed to deserialize incoming message because of unsupported message type: ";

        public NotSupportedMessageTypeException(MessageType messageType) {
            super(MESSAGE + messageType);
        }
    }
}