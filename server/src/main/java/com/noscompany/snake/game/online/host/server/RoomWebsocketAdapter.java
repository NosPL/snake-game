package com.noscompany.snake.game.online.host.server;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.room.PlayerName;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;

@AllArgsConstructor
class RoomWebsocketAdapter implements WebsocketEventHandler {
    private final RoomApiForRemoteClients roomApiForRemoteClients;
    private final MessageDeserializer messageDeserializer;

    @Override
    public void newClientConnected(RemoteClientId remoteClientId) {
        roomApiForRemoteClients.initializeClientState(remoteClientId);
    }

    @Override
    public void messageReceived(RemoteClientId remoteClientId, String message) {
        messageDeserializer
                .deserialize(remoteClientId, message)
                .peek(deserializedMessage -> deserializedMessage.applyTo(roomApiForRemoteClients));
    }

    @Override
    public void clientDisconnected(RemoteClientId remoteClientId) {
        roomApiForRemoteClients.leaveRoom(remoteClientId);
    }

    static RoomWebsocketAdapter create(RoomApiForRemoteClients roomApiForRemoteClients) {
        return new RoomWebsocketAdapter(roomApiForRemoteClients, new MessageDeserializer());
    }
}

@Slf4j
class MessageDeserializer {

    Try<DeserializedMessage> deserialize(RemoteClientId remoteClientId, String serializedMessage) {
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

    private DeserializedMessage mapToObject(RemoteClientId remoteClientId, DocumentContext parsedMessage, OnlineMessage.MessageType messageType) {
        return switch (messageType) {
            case CHANGE_SNAKE_DIRECTION -> changeSnakeDirection(remoteClientId, parsedMessage);
            case SEND_CHAT_MESSAGE -> toSendChat(remoteClientId, parsedMessage);
            case TAKE_A_SEAT -> takeASeat(remoteClientId, parsedMessage);
            case CHANGE_GAME_OPTIONS -> changeGameOptions(remoteClientId, parsedMessage);
            case FREE_UP_A_SEAT -> new DeserializedMessage.FreeUpASeat(remoteClientId);
            case START_GAME -> new DeserializedMessage.StartGame(remoteClientId);
            case CANCEL_GAME -> new DeserializedMessage.CancelGame(remoteClientId);
            case PAUSE_GAME -> new DeserializedMessage.PauseGame(remoteClientId);
            case RESUME_GAME -> new DeserializedMessage.ResumeGame(remoteClientId);
            case ENTER_THE_ROOM -> toEnterTheRoom(remoteClientId, parsedMessage);
            default -> throw new RuntimeException("Failed to deserialize message because of unknown message type: " + messageType);
        };
    }

    private DeserializedMessage toSendChat(RemoteClientId remoteClientId, DocumentContext parsedMessage) {
        String messageContent = parsedMessage.read("$.messageContent");
        return new DeserializedMessage.SendChatMessage(remoteClientId, messageContent);
    }

    private DeserializedMessage toEnterTheRoom(RemoteClientId remoteClientId, DocumentContext parsedMessage) {
        String userName = parsedMessage.read("$.userName");
        return new DeserializedMessage.EnterRoom(remoteClientId, userName);
    }

    private DeserializedMessage changeGameOptions(RemoteClientId remoteClientId, DocumentContext parsedMessage) {
        String gridSizeStr = parsedMessage.read("$.gridSize");
        GridSize gridSize = GridSize.valueOf(gridSizeStr);
        String gameSpeedStr = parsedMessage.read("$.gameSpeed");
        GameSpeed gameSpeed = GameSpeed.valueOf(gameSpeedStr);
        String wallsStr = parsedMessage.read("$.walls");
        Walls walls = Walls.valueOf(wallsStr);
        GameOptions gameOptions = new GameOptions(gridSize, gameSpeed, walls);
        return new DeserializedMessage.ChangeGameOptions(remoteClientId, gameOptions);
    }

    private DeserializedMessage changeSnakeDirection(RemoteClientId remoteClientId, DocumentContext parsedMessage) {
        String snakeDirectionStr = parsedMessage.read("$.direction");
        Direction direction = Direction.valueOf(snakeDirectionStr);
        return new DeserializedMessage.ChangeSnakeDirection(remoteClientId, direction);
    }

    private DeserializedMessage takeASeat(RemoteClientId remoteClientId, DocumentContext parsedMessage) {
        String snakeNumberStr = parsedMessage.read("$.playerNumber");
        PlayerNumber playerNumber = PlayerNumber.valueOf(snakeNumberStr);
        return new DeserializedMessage.TakeASeat(remoteClientId, playerNumber);
    }
}

@RequiredArgsConstructor
abstract class DeserializedMessage {
    protected final RemoteClientId remoteClientId;

    abstract void applyTo(RoomApiForRemoteClients handler);

    static class EnterRoom extends DeserializedMessage {
        private final String userName;

        EnterRoom(RemoteClientId remoteClientId, String userName) {
            super(remoteClientId);
            this.userName = userName;
        }

        @Override
        void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.enterRoom(remoteClientId, new PlayerName(userName));
        }
    }

    static class TakeASeat extends DeserializedMessage {
        private final PlayerNumber playerNumber;

        TakeASeat(RemoteClientId remoteClientId, PlayerNumber playerNumber) {
            super(remoteClientId);
            this.playerNumber = playerNumber;
        }

        @Override
        void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.takeASeat(remoteClientId, playerNumber);
        }
    }

    static class FreeUpASeat extends DeserializedMessage {

        FreeUpASeat(RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
        void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.freeUpSeat(remoteClientId);
        }
    }

    static class ChangeGameOptions extends DeserializedMessage {
        private final GameOptions gameOptions;

        ChangeGameOptions(RemoteClientId remoteClientId, GameOptions gameOptions) {
            super(remoteClientId);
            this.gameOptions = gameOptions;
        }

        @Override
        void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.changeGameOptions(remoteClientId, gameOptions);
        }
    }

    static class ChangeSnakeDirection extends DeserializedMessage {
        private final Direction direction;

        ChangeSnakeDirection(RemoteClientId remoteClientId, Direction direction) {
            super(remoteClientId);
            this.direction = direction;
        }

        @Override
        void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.changeDirection(remoteClientId, direction);
        }
    }

    static class StartGame extends DeserializedMessage {

        StartGame(RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
        void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.startGame(remoteClientId);
        }
    }

    static class CancelGame extends DeserializedMessage {

        CancelGame(RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
        void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.cancelGame(remoteClientId);
        }
    }

    static class PauseGame extends DeserializedMessage {

        PauseGame(RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
        void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.pauseGame(remoteClientId);
        }
    }

    static class ResumeGame extends DeserializedMessage {

        ResumeGame(RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
        void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.resumeGame(remoteClientId);
        }
    }

    static class SendChatMessage extends DeserializedMessage {
        private final String messageContent;

        SendChatMessage(RemoteClientId remoteClientId, String messageContent) {
            super(remoteClientId);
            this.messageContent = messageContent;
        }

        @Override
        void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.sendChatMessage(remoteClientId, messageContent);
        }
    }
}