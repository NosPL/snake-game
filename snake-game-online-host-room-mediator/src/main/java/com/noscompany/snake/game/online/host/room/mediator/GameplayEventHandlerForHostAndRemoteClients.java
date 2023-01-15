package com.noscompany.snake.game.online.host.room.mediator;

import com.noscompany.snake.game.online.contract.messages.game.events.*;
import com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForHost;
import com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForRemoteClients;
import lombok.AllArgsConstructor;
import snake.game.gameplay.SnakeGameEventHandler;

@AllArgsConstructor
class GameplayEventHandlerForHostAndRemoteClients implements SnakeGameEventHandler {
    private final RoomEventHandlerForHost host;
    private final RoomEventHandlerForRemoteClients remoteClients;

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameStarted event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameContinues event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameFinished event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameCancelled event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GamePaused event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameResumed event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }
}