package com.noscompany.snake.game.online.host.room.mediator;

import com.noscompany.snake.game.online.host.room.RoomCreator;
import com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForHost;
import com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForRemoteClients;
import dagger.Module;
import dagger.Provides;


@Module
public class RoomMediatorConfiguration {

    @Provides
    public RoomMediator roomMediator(RoomEventHandlerForHost roomEventHandlerForHost,
                              RoomEventHandlerForRemoteClients roomEventHandlerForRemoteClients) {
        var gameplayEventHandler = new GameplayEventHandlerForHostAndRemoteClients(roomEventHandlerForHost, roomEventHandlerForRemoteClients);
        var room = RoomCreator.create(gameplayEventHandler);
        var roomEventDispatcher = new EventDispatcher(roomEventHandlerForHost, roomEventHandlerForRemoteClients);
        return new RoomMediator(room, roomEventDispatcher);
    }

    @Provides
    public RoomMediatorForHost roomEventHandlerForHost(RoomMediator roomMediator) {
        return roomMediator;
    }

    @Provides
    public RoomMediatorForRemoteClients roomMediatorForRemoteClients(RoomMediator roomMediator) {
        return roomMediator;
    }
}