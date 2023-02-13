package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.snake.game.online.host.RoomEventHandlerForHost;
import com.noscompany.snake.game.online.contract.messages.room.PlayersLimit;
import com.noscompany.snake.game.online.host.room.RoomCreator;
import com.noscompany.snake.game.online.host.server.RoomEventHandlerForRemoteClients;
import snake.game.gameplay.GameplayCreator;

import java.util.concurrent.*;


public class MediatorConfiguration {

    public Mediator mediator(RoomEventHandlerForHost handlerForHost,
                             RoomEventHandlerForRemoteClients handlerForRemoteClients,
                             RoomCreator roomCreator,
                             PlayersLimit playersLimit,
                             GameplayCreator gameplayCreator) {
        return mediator(executorService(), handlerForHost, handlerForRemoteClients, roomCreator, playersLimit, gameplayCreator);
    }

    public Mediator mediator(ExecutorService executorService,
                             RoomEventHandlerForHost handlerForHost,
                             RoomEventHandlerForRemoteClients handlerForRemoteClients,
                             RoomCreator roomCreator,
                             PlayersLimit playersLimit,
                             GameplayCreator gameplayCreator) {
        var eventDispatcher = new EventDispatcher(handlerForHost, handlerForRemoteClients);
        var room = roomCreator.createRoom(eventDispatcher, gameplayCreator, playersLimit);
        var commandHandler = new CommandHandler(room, eventDispatcher);
        return new CommandQueue(executorService, commandHandler);
    }

    private ExecutorService executorService() {
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(100),
                new ThreadPoolExecutor.DiscardPolicy());
    }
}