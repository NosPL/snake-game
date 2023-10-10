package com.noscompany.snake.game.online.host.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.user.registry.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;

@Slf4j
@AllArgsConstructor
class MessageDeserializer {
    private final ObjectMapper objectMapper;

    Try<OnlineMessage> deserialize(String serializedMessage) {
        try {
            var parsedMessage = JsonPath.parse(serializedMessage);
            var messageType = getMessageType(parsedMessage);
            var deserializedMessage = mapToObject(messageType, serializedMessage);
            log.info("message deserialized, passing it to command handler");
            return success(deserializedMessage);
        } catch (Exception e) {
            return failure(e);
        }
    }

    private OnlineMessage.MessageType getMessageType(DocumentContext parsedMessage) {
        String messageTypeName = parsedMessage.read("$.messageType");
        return OnlineMessage.MessageType.valueOf(messageTypeName);
    }

    @SneakyThrows
    private OnlineMessage mapToObject(OnlineMessage.MessageType messageType, String serializedMessage) {
        return switch (messageType) {
            case CHANGE_SNAKE_DIRECTION -> objectMapper.readValue(serializedMessage, ChangeSnakeDirection.class);
            case SEND_CHAT_MESSAGE -> objectMapper.readValue(serializedMessage, SendChatMessage.class);
            case TAKE_A_SEAT -> objectMapper.readValue(serializedMessage, TakeASeat.class);
            case CHANGE_GAME_OPTIONS -> objectMapper.readValue(serializedMessage, ChangeGameOptions.class);
            case FREE_UP_A_SEAT -> objectMapper.readValue(serializedMessage, FreeUpASeat.class);
            case START_GAME -> objectMapper.readValue(serializedMessage, StartGame.class);
            case CANCEL_GAME -> objectMapper.readValue(serializedMessage, CancelGame.class);
            case PAUSE_GAME -> objectMapper.readValue(serializedMessage, PauseGame.class);
            case RESUME_GAME -> objectMapper.readValue(serializedMessage, ResumeGame.class);
            case ENTER_THE_ROOM -> objectMapper.readValue(serializedMessage, EnterRoom.class);
            default -> throw new RuntimeException("Failed to deserialize message because of unknown message type: " + messageType);
        };
    }

    private OnlineMessage toSendChat(UserId remoteClientId, DocumentContext parsedMessage) {
        String messageContent = parsedMessage.read("$.messageContent");
        return new SendChatMessage(remoteClientId, messageContent);
    }

    private OnlineMessage toEnterTheRoom(UserId remoteClientId, DocumentContext parsedMessage) {
        String userName = parsedMessage.read("$.userName");
        return new EnterRoom(remoteClientId, new UserName(userName));
    }

    private OnlineMessage changeGameOptions(UserId remoteClientId, DocumentContext parsedMessage) {
        String gridSizeStr = parsedMessage.read("$.gridSize");
        GridSize gridSize = GridSize.valueOf(gridSizeStr);
        String gameSpeedStr = parsedMessage.read("$.gameSpeed");
        GameSpeed gameSpeed = GameSpeed.valueOf(gameSpeedStr);
        String wallsStr = parsedMessage.read("$.walls");
        Walls walls = Walls.valueOf(wallsStr);
        return new ChangeGameOptions(remoteClientId, gridSize, gameSpeed, walls);
    }

    private ChangeSnakeDirection changeSnakeDirection(UserId remoteClientId, DocumentContext parsedMessage) {
        String snakeDirectionStr = parsedMessage.read("$.direction");
        Direction direction = Direction.valueOf(snakeDirectionStr);
        return new ChangeSnakeDirection(remoteClientId, direction);
    }

    private TakeASeat takeASeat(UserId remoteClientId, DocumentContext parsedMessage) {
        String snakeNumberStr = parsedMessage.read("$.playerNumber");
        PlayerNumber playerNumber = PlayerNumber.valueOf(snakeNumberStr);
        return new TakeASeat(remoteClientId, playerNumber);
    }
}
