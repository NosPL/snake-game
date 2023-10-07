package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import com.noscompany.snake.game.online.host.RoomEventHandlerForHost;
import com.noscompany.snake.game.online.host.room.RoomCreator;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.utils.monitored.executor.service.MonitoredExecutorServiceCreator;
import snake.game.gameplay.GameplayCreator;

import java.util.concurrent.*;


public class MediatorConfiguration {
    private final MonitoredExecutorServiceCreator monitoredExecutorServiceCreator = new MonitoredExecutorServiceCreator();

    public Mediator mediator(RoomEventHandlerForHost hostEventHandler,
                             Server server,
                             RoomCreator roomCreator,
                             UsersCountLimit usersCountLimit,
                             GameplayCreator gameplayCreator) {
        return mediator(monitoredExecutorService(), hostEventHandler, server, roomCreator, usersCountLimit, gameplayCreator);
    }

    public Mediator mediator(ExecutorService executorService,
                             RoomEventHandlerForHost hostEventHandler,
                             Server server,
                             RoomCreator roomCreator,
                             UsersCountLimit usersCountLimit,
                             GameplayCreator gameplayCreator) {
        var eventDispatcher = new EventDispatcher(hostEventHandler, server);
        var room = roomCreator.createRoom(eventDispatcher, gameplayCreator, usersCountLimit);
        var commandHandler = new CommandHandler(server, room, eventDispatcher);
        return new CommandQueue(executorService, commandHandler);
    }

    private ExecutorService monitoredExecutorService() {
        return monitoredExecutorServiceCreator.create(
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