package com.noscompany.snake.game.online.websocket;

import com.noscompany.snake.game.online.host.server.ports.WebsocketCreator;

public class WebsocketConfiguration {
    public WebsocketCreator websocketCreator() {
        return new NettosphereWebsocketCreator();
    }
}