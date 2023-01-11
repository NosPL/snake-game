package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.host.SnakeOnlineHost;
import com.noscompany.snake.game.online.host.SnakeOnlineHostConfiguration;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorConfiguration;
import com.noscompany.snake.game.online.host.server.nettosphere.ServerConfiguration;

class OnlineHostCreator {

    static SnakeOnlineHost create() {
        GuiOnlineHostEventHandler guiOnlineHostEventHandler = GuiOnlineHostEventHandler.instance();
        var server = new ServerConfiguration().createServer();
        var roomMediator = new RoomMediatorConfiguration().roomMediator(guiOnlineHostEventHandler, server);
        return new SnakeOnlineHostConfiguration().snakeOnlineHost(server, guiOnlineHostEventHandler, roomMediator, roomMediator);
    }
}