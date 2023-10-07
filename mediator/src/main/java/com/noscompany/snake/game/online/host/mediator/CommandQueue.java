package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.room.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.server.StartServer;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import com.noscompany.snake.game.utils.monitored.executor.service.NamedRunnable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

import static com.noscompany.snake.game.utils.monitored.executor.service.NamedRunnable.namedTask;

@Slf4j
@AllArgsConstructor
class CommandQueue implements Mediator {
    private final ExecutorService executor;
    private final CommandHandler commandHandler;

    @Override
    public void startServer(StartServer command) {
        log.info("host puts 'start server' command in queue");
        var namedTask = namedTask("start server", () -> commandHandler.startServer(command, this));
        executor.submit(namedTask);
    }

    public void enter(EnterRoom command) {
        log.info("host puts 'enter room' command in queue");
        var namedTask = namedTask("enter the room", () -> commandHandler.enter(command));
        executor.submit(namedTask);
    }

    public void sendChatMessage(SendChatMessage command) {
        log.info("host puts 'send chat message' command in queue");
        var namedTask = namedTask("send chat message", () -> commandHandler.sendChatMessage(command));
        executor.submit(namedTask);
    }

    public void cancelGame(CancelGame command) {
        log.info("host puts 'cancel game' command in queue");
        var namedTask = namedTask("cancel game", () -> commandHandler.cancelGame(command));
        executor.submit(namedTask);
    }

    public void changeSnakeDirection(ChangeSnakeDirection command) {
        log.debug("host puts 'change snake direction' command in queue");
        var namedTask = namedTask("change snake direction", () -> commandHandler.changeSnakeDirection(command));
        executor.submit(namedTask);
    }

    public void pauseGame(PauseGame command) {
        log.info("host puts 'pause game' command in queue");
        var namedTask = namedTask("pause game", () -> commandHandler.pauseGame(command));
        executor.submit(namedTask);
    }

    public void resumeGame(ResumeGame command) {
        log.info("host puts 'resume game' command in queue");
        var namedTask = namedTask("resume game", () -> commandHandler.resumeGame(command));
        executor.submit(namedTask);
    }

    public void startGame(StartGame command) {
        log.info("host puts 'start game' command in queue");
        var namedTask = namedTask("start game", () -> commandHandler.startGame(command));
        executor.submit(namedTask);
    }

    public void changeGameOptions(ChangeGameOptions command) {
        log.info("host puts 'change game options' command in queue");
        var namedTask = namedTask("change game options", () -> commandHandler.changeGameOptions(command));
        executor.submit(namedTask);
    }

    public void freeUpASeat(FreeUpASeat command) {
        log.info("host puts 'free up a seat' command in queue");
        var namedTask = namedTask("free up a seat", () -> commandHandler.freeUpASeat(command));
        executor.submit(namedTask);
    }

    public void takeASeat(TakeASeat command) {
        log.info("host puts 'take a seat' command in queue");
        var namedTask = namedTask("take a seat", () -> commandHandler.takeASeat(command));
        executor.submit(namedTask);
    }

    public void shutdown() {
        log.info("shutting mediator command queue down");
        var namedTask = namedTask("shutdown host", commandHandler::shutdown);
        executor.submit(namedTask);
        executor.shutdown();
    }

    @Override
    public void sendChatMessage(RemoteClientId remoteClientId, String messageContent) {
        log.info("remote client with id {} puts 'send chat message' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.sendChatMessage(remoteClientId, messageContent));
    }

    @Override
    public void cancelGame(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'cancel game' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.cancelGame(remoteClientId));
    }

    @Override
    public void changeDirection(RemoteClientId remoteClientId, Direction direction) {
        log.debug("remote client with id {} puts 'change snake direction' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.changeDirection(remoteClientId, direction));
    }

    @Override
    public void pauseGame(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'pause game' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.pauseGame(remoteClientId));
    }

    @Override
    public void resumeGame(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'resume game' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.resumeGame(remoteClientId));
    }

    @Override
    public void startGame(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'start game' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.startGame(remoteClientId));
    }

    @Override
    public void changeGameOptions(RemoteClientId remoteClientId, GameOptions gameOptions) {
        log.info("remote client with id {} puts 'change game options' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.changeGameOptions(remoteClientId, gameOptions));
    }

    @Override
    public void freeUpSeat(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'free up a seat' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.freeUpSeat(remoteClientId));
    }

    @Override
    public void takeASeat(RemoteClientId remoteClientId, PlayerNumber playerNumber) {
        log.info("remote client with id {} puts 'take a seat' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.takeASeat(remoteClientId, playerNumber));
    }

    @Override
    public void enterRoom(RemoteClientId remoteClientId, UserName userName) {
        log.info("remote client with id {} puts 'enter room' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.enterRoom(remoteClientId, userName));
    }

    @Override
    public void leaveRoom(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'leave room' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.leaveRoom(remoteClientId));
    }

    @Override
    public void initializeClientState(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'initialize client state' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.initializeClientState(remoteClientId));
    }
}