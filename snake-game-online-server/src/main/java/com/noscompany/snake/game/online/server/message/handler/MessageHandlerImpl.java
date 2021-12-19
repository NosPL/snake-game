package com.noscompany.snake.game.online.server.message.handler;

import com.noscompany.snake.game.online.server.message.handler.message.mapper.MessageMapper;
import com.noscompany.snake.game.online.server.room.Room;
import lombok.AllArgsConstructor;
import org.atmosphere.cpr.AtmosphereResource;
import snake.game.core.dto.*;

@AllArgsConstructor
class MessageHandlerImpl implements MessageHandler {
    private final Room room;
    private final MessageSender messageSender;
    private final MessageMapper messageMapper;

    @Override
    public void handle(AtmosphereResource r, String stringMsg) {
        Message message = messageMapper.map(stringMsg);
        message.onRoomEnter(userName -> enterRoom(r, userName));
        message.onTakeASeat(snakeNumber -> takeASeat(r, snakeNumber));
        message.onFreeUpASeat(() -> freeUpASeat(r));
        message.onChangeGameOptions((gridSize, gameSpeed, walls) -> changeGameOptions(r, gridSize, gameSpeed, walls));
        message.onStartGame(() -> room.startGame(r.uuid()));
        message.onChangeSnakeDirection(direction -> changeSnakeDirection(r, direction));
        message.onCancelGame(() -> room.cancelGame(r.uuid()));
        message.onPauseGame(() -> room.pauseGame(r.uuid()));
        message.onResumeGame(() -> room.resumeGame(r.uuid()));
        message.onSendChatMessage(chatMessage -> sendChatMessage(r, chatMessage));
        message.onUnknownMessage(unknownMsg -> handleUnknownMessage(r, unknownMsg));
    }

    private void enterRoom(AtmosphereResource r, String userName) {
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

    }

    @Override
    public void userDisconnected(String connectionId) {
        room
                .removeUser(connectionId)
                .peek(messageSender::sendToAll);
    }
}