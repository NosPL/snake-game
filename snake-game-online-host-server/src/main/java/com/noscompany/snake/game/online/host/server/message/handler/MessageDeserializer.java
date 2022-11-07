package com.noscompany.snake.game.online.host.server.message.handler;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage.MessageType;
import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.server.message.handler.internal.serializer.NotSupportedMessageTypeException;
import io.vavr.control.Try;

import static com.jayway.jsonpath.JsonPath.parse;
import static com.noscompany.snake.game.online.host.server.message.handler.DeserializedMessage.*;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;

class MessageDeserializer {

    Try<DeserializedMessage> deserialize(RemoteClientId remoteClientId, String serializedMessage) {
        try {
            var parsedMessage = JsonPath.parse(serializedMessage);
            var messageType = getMessageType(parsedMessage);
            var deserializedMessage = mapToObject(remoteClientId, parsedMessage, messageType);
            return success(deserializedMessage);
        } catch (Exception e) {
            return failure(e);
        }
    }

    private MessageType getMessageType(DocumentContext parsedMessage) {
        String messageTypeName = parsedMessage.read("$.messageType");
        return MessageType.valueOf(messageTypeName);
    }

    private DeserializedMessage mapToObject(RemoteClientId remoteClientId, DocumentContext parsedMessage, MessageType messageType) {
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
            default -> throw new NotSupportedMessageTypeException(messageType);
        };
    }

    private DeserializedMessage toSendChat(RemoteClientId remoteClientId, DocumentContext parsedMessage) {
        String messageContent = parsedMessage.read("$.messageContent");
        return new SendChatMessage(remoteClientId, messageContent);
    }

    private DeserializedMessage toEnterTheRoom(RemoteClientId remoteClientId, DocumentContext parsedMessage) {
        String userName = parsedMessage.read("$.userName");
        return new EnterRoom(remoteClientId, userName);
    }

    private DeserializedMessage changeGameOptions(RemoteClientId remoteClientId, DocumentContext parsedMessage) {
        String gridSizeStr = parsedMessage.read("$.gridSize");
        GridSize gridSize = GridSize.valueOf(gridSizeStr);
        String gameSpeedStr = parsedMessage.read("$.gameSpeed");
        GameSpeed gameSpeed = GameSpeed.valueOf(gameSpeedStr);
        String wallsStr = parsedMessage.read("$.walls");
        Walls walls = Walls.valueOf(wallsStr);
        GameOptions gameOptions = new GameOptions(gridSize, gameSpeed, walls);
        return new ChangeGameOptions(remoteClientId, gameOptions);
    }

    private DeserializedMessage changeSnakeDirection(RemoteClientId remoteClientId, DocumentContext parsedMessage) {
        String snakeDirectionStr = parsedMessage.read("$.direction");
        Direction direction = Direction.valueOf(snakeDirectionStr);
        return new ChangeSnakeDirection(remoteClientId, direction);
    }

    private DeserializedMessage takeASeat(RemoteClientId remoteClientId, DocumentContext parsedMessage) {
        String snakeNumberStr = parsedMessage.read("$.playerNumber");
        PlayerNumber playerNumber = PlayerNumber.valueOf(snakeNumberStr);
        return new TakeASeat(remoteClientId, playerNumber);
    }
}