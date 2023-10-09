package com.noscompany.snake.game.online.host.server;

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
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;

@Slf4j
class MessageDeserializer {

    Try<OnlineMessage> deserialize(UserId remoteClientId, String serializedMessage) {
        try {
            var parsedMessage = JsonPath.parse(serializedMessage);
            var messageType = getMessageType(parsedMessage);
            var deserializedMessage = mapToObject(remoteClientId, parsedMessage, messageType);
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

    private OnlineMessage mapToObject(UserId remoteClientId, DocumentContext parsedMessage, OnlineMessage.MessageType messageType) {
        return switch (messageType) {
            case CHANGE_SNAKE_DIRECTION -> changeSnakeDirection(remoteClientId, parsedMessage);
            case SEND_CHAT_MESSAGE -> toSendChat(remoteClientId, parsedMessage);
            case TAKE_A_SEAT -> takeASeat(remoteClientId, parsedMessage);
            case CHANGE_GAME_OPTIONS -> changeGameOptions(remoteClientId, parsedMessage);
            case FREE_UP_A_SEAT -> new FreeUpASeat(remoteClientId);
            case START_GAME -> new StartGame(remoteClientId);
            case CANCEL_GAME -> new CancelGame(remoteClientId);
            case PAUSE_GAME -> new PauseGame(remoteClientId);
            case RESUME_GAME -> new ResumeGame(remoteClientId);
            case ENTER_THE_ROOM -> toEnterTheRoom(remoteClientId, parsedMessage);
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
