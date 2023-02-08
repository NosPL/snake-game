package com.noscompany.snake.game.online.host.room.mediator;

import com.noscompany.snake.game.online.host.RoomEventHandlerForHost;
import com.noscompany.snake.game.online.contract.messages.room.PlayersLimit;
import com.noscompany.snake.game.online.host.room.RoomCreator;
import com.noscompany.snake.game.online.host.server.RoomEventHandlerForRemoteClients;
import snake.game.gameplay.SnakeGameplayConfiguration;


public class RoomMediatorConfiguration {

    public RoomMediator roomMediator(RoomEventHandlerForHost roomEventHandlerForHost,
                                     RoomEventHandlerForRemoteClients roomEventHandlerForRemoteClients,
                                     RoomCreator roomCreator,
                                     PlayersLimit playersLimit) {
        var gameplayEventHandler = new GameplayEventHandlerForHostAndRemoteClients(roomEventHandlerForHost, roomEventHandlerForRemoteClients);
        var snakeGameplayCreator = new SnakeGameplayConfiguration().snakeGameplayCreator();
        var room = roomCreator.createRoom(gameplayEventHandler, snakeGameplayCreator, playersLimit);
        var roomEventDispatcher = new EventDispatcher(roomEventHandlerForHost, roomEventHandlerForRemoteClients);
        return new RoomMediator(room, roomEventDispatcher);
    }
}