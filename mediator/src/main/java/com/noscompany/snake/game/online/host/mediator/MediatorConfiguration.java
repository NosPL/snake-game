package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.room.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.server.ShutdownHost;
import com.noscompany.snake.game.online.contract.messages.server.StartServer;
import com.noscompany.snake.game.online.host.room.RoomCreator;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import com.noscompany.snake.game.utils.monitored.executor.service.MonitoredExecutorServiceCreator;
import snake.game.gameplay.GameplayCreator;

import java.util.concurrent.*;


public class MediatorConfiguration {

    public RoomApiForRemoteClients roomApiForRemoteClients(MessagePublisher messagePublisher,
                                                           Server server,
                                                           RoomCreator roomCreator,
                                                           UsersCountLimit usersCountLimit,
                                                           GameplayCreator gameplayCreator) {
        return roomApiForRemoteClients(monitoredExecutorService(), messagePublisher, server, roomCreator, usersCountLimit, gameplayCreator);
    }

    public RoomApiForRemoteClients roomApiForRemoteClients(ExecutorService executorService,
                                                           MessagePublisher messagePublisher,
                                                           Server server,
                                                           RoomCreator roomCreator,
                                                           UsersCountLimit usersCountLimit,
                                                           GameplayCreator gameplayCreator) {
        var eventDispatcher = new EventDispatcher(messagePublisher, server);
        var room = roomCreator.createRoom(eventDispatcher, gameplayCreator, usersCountLimit);
        var commandHandler = new CommandHandler(server, room, eventDispatcher);
        var commandQueue = new CommandQueue(executorService, commandHandler);
        var subscription = createSubscription(commandQueue);
        messagePublisher.subscribe(subscription);
        return commandQueue;
    }

    Subscription createSubscription(CommandQueue commandQueue) {
        return new Subscription()
                .subscriberName("mediator")
                .toMessage(StartServer.class, commandQueue::startServerAsHost)
                .toMessage(EnterRoom.class, commandQueue::enterAsHost)
                .toMessage(SendChatMessage.class, commandQueue::sendChatMessageAsHost)
                .toMessage(CancelGame.class, commandQueue::cancelGameAsHost)
                .toMessage(ChangeSnakeDirection.class, commandQueue::changeSnakeDirectionAsHost)
                .toMessage(PauseGame.class, commandQueue::pauseGameAsHost)
                .toMessage(ResumeGame.class, commandQueue::resumeGameAsHost)
                .toMessage(StartGame.class, commandQueue::startGameAsHost)
                .toMessage(ChangeGameOptions.class, commandQueue::changeGameOptionsAsHost)
                .toMessage(FreeUpASeat.class, commandQueue::freeUpASeatAsHost)
                .toMessage(TakeASeat.class, commandQueue::takeASeatAsHost)
                .toMessage(ShutdownHost.class, commandQueue::shutdown);
    }

    private ExecutorService monitoredExecutorService() {
        return new MonitoredExecutorServiceCreator().create(
                1, 1,
                new LinkedBlockingDeque<>(100),
                threadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());
    }

    private ThreadFactory threadFactory() {
        return runnable -> {
            Thread thread = new Thread(runnable, "mediator-command-queue-thread");
            thread.setDaemon(true);
            return thread;
        };
    }
}