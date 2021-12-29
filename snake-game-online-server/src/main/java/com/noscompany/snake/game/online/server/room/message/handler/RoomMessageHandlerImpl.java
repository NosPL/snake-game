package com.noscompany.snake.game.online.server.room.message.handler;

import com.noscompany.snake.game.commons.messages.dto.RoomState;
import com.noscompany.snake.game.commons.messages.events.lobby.FailedToStartGame;
import com.noscompany.snake.game.online.server.room.message.handler.mapper.MessageMapper;
import com.noscompany.snake.game.online.server.room.message.handler.sender.MessageSender;
import com.noscompany.snake.game.online.server.room.room.Room;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;
import snake.game.core.dto.*;

@AllArgsConstructor
@Slf4j
class RoomMessageHandlerImpl implements RoomMessageHandler {
    private final Room room;
    private final MessageSender messageSender;
    private final MessageMapper messageMapper;

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

    private void takeASeat(AtmosphereResource r, SnakeNumber snakeNumber) {
        room.takeASeat(r.uuid(), snakeNumber)
                .peek(messageSender::sendToAll)
                .peekLeft(failure -> messageSender.send(r, failure));
    }

    private void changeGameOptions(AtmosphereResource r, GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        room.changeGameOptions(r.uuid(), gridSize, gameSpeed, walls)
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

    @Override
    public void removeUserById(String userId) {
        room
                .removeUserById(userId)
                .peek(messageSender::sendToAll);
    }

    @Override
    public RoomState getRoomState() {
        return room.getState();
    }
}