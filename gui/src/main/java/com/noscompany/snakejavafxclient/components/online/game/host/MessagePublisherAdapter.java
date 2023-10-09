package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.user.registry.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.contract.messages.host.HostGotShutdown;
import com.noscompany.snake.game.online.contract.messages.server.commands.StartServer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
final class MessagePublisherAdapter {
    private final UserId hostId;
    private final MessagePublisher messagePublisher;

    public void startServer(ServerParams serverParams) {
        messagePublisher.publishMessage(new StartServer(serverParams));
    }

    public void enterRoom(UserName userName) {
        messagePublisher.publishMessage(new EnterRoom(hostId, userName));
    }

    public void sendChatMessage(String messageContent) {
        messagePublisher.publishMessage(new SendChatMessage(hostId, messageContent));
    }

    public void cancelGame() {
        messagePublisher.publishMessage(new CancelGame(hostId));
    }

    public void changeSnakeDirection(Direction direction) {
        messagePublisher.publishMessage(new ChangeSnakeDirection(hostId, direction));
    }

    public void pauseGame() {
        messagePublisher.publishMessage(new PauseGame(hostId));
    }

    public void resumeGame() {
        messagePublisher.publishMessage(new ResumeGame(hostId));
    }

    public void startGame() {
        messagePublisher.publishMessage(new StartGame(hostId));
    }

    public void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        messagePublisher.publishMessage(new ChangeGameOptions(hostId, gridSize, gameSpeed, walls));
    }

    public void freeUpASeat() {
        messagePublisher.publishMessage(new FreeUpASeat(hostId));
    }

    public void takeASeat(PlayerNumber playerNumber) {
        messagePublisher.publishMessage(new TakeASeat(hostId, playerNumber));
    }

    public void shutDownHost() {
        messagePublisher.publishMessage(new HostGotShutdown(hostId));
        messagePublisher.shutdown();
    }
}