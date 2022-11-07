package com.noscompany.snake.game.online.host.server.message.handler;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import com.noscompany.snake.game.online.contract.messages.room.RoomState;
import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.room.FailedToConnectToRoom;
import com.noscompany.snake.game.online.host.room.Room;
import com.noscompany.snake.game.online.host.server.message.handler.internal.serializer.Message;
import com.noscompany.snake.game.online.host.server.message.handler.internal.serializer.MessageMapper;
import com.noscompany.snake.game.online.host.server.message.handler.internal.sender.MessageSender;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;

@AllArgsConstructor
@Slf4j
public class RoomMessageHandlerImpl implements RoomMessageHandler {
    private final Room room;
    private final MessageSender messageSender;
    private final MessageMapper messageMapper;

    @Override
    public void newUserConnected(AtmosphereResource resource) {
        String userId = resource.uuid();
        log.info("new user connected: {}", userId);
        if (room.getState().isFull()) {
            log.info("connection limit reached for room");
            disconnect(resource);
        }
    }

    @Override
    public void handle(AtmosphereResource r, String stringMessage) {
        Message message = messageMapper.map(stringMessage);
        message.onRoomEnter(userName -> enterTheRoom(r, userName));
        message.onTakeASeat(snakeNumber -> takeASeat(r, snakeNumber));
        message.onFreeUpASeat(() -> freeUpASeat(r));
        message.onChangeGameOptions((gridSize, gameSpeed, walls) -> changeGameOptions(r, gridSize, gameSpeed, walls));
        message.onStartGame(() -> startGame(r));
        message.onChangeSnakeDirection(direction -> changeSnakeDirection(r, direction));
        message.onCancelGame(() -> room.cancelGame(r.uuid()));
        message.onPauseGame(() -> room.pauseGame(r.uuid()));
        message.onResumeGame(() -> room.resumeGame(r.uuid()));
        message.onSendChatMessage(chatMessage -> sendChatMessage(r, chatMessage));
        message.onUnknownMessage(unknownMsg -> handleUnknownMessage(r, unknownMsg));
    }

    @Override
    public void removeUserById(String userId) {
        room
                .removeUserById(userId)
                .peek(messageSender::sendToAll);
    }

    private void disconnect(AtmosphereResource resource) {
        messageSender.send(resource, FailedToConnectToRoom.roomIsFull());
        log.info("closing resource with id {}", resource.uuid());
        Try
                .run(resource::close)
                .onFailure(e -> log.warn("failed to close resource", e));
    }

    @Override
    public RoomState getRoomState() {
        return room.getState();
    }

    private Option<FailedToStartGame> startGame(AtmosphereResource r) {
        return room.startGame(r.uuid())
                .peek(failure -> messageSender.send(r, failure));
    }

    private void enterTheRoom(AtmosphereResource r, String userName) {
        room.enter(r.uuid(), userName)
                .peek(messageSender::sendToAll)
                .peekLeft(failure -> messageSender.send(r, failure));
    }

    private void freeUpASeat(AtmosphereResource r) {
        room.freeUpASeat(r.uuid())
                .peek(messageSender::sendToAll)
                .peekLeft(failure -> messageSender.send(r, failure));
    }

    private void takeASeat(AtmosphereResource r, PlayerNumber playerNumber) {
        room.takeASeat(r.uuid(), playerNumber)
                .peek(messageSender::sendToAll)
                .peekLeft(failure -> messageSender.send(r, failure));
    }

    private void changeGameOptions(AtmosphereResource r, GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        room.changeGameOptions(r.uuid(), new GameOptions(gridSize, gameSpeed, walls))
                .peek(messageSender::sendToAll)
                .peekLeft(failure -> messageSender.send(r, failure));
    }

    private void changeSnakeDirection(AtmosphereResource r, Direction direction) {
        room.changeSnakeDirection(r.uuid(), direction);
    }

    private void sendChatMessage(AtmosphereResource r, String message) {
        room.sendChatMessage(r.uuid(), message)
                .peek(messageSender::sendToAll)
                .peekLeft(failure -> messageSender.send(r, failure));
    }

    private void handleUnknownMessage(AtmosphereResource r, String unknownMsg) {
        log.warn("resource with id: {}, sent unknown message: {}", r.uuid(), unknownMsg);
    }
}