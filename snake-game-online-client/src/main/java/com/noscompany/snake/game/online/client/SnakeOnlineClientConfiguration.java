package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.online.client.internal.state.not.connected.Disconnected;
import dagger.Module;
import dagger.Provides;

@Module
public class SnakeOnlineClientConfiguration {

    @Provides
    public SnakeOnlineClient create(ClientEventHandler eventHandler) {
        return new SnakeOnlineClientImpl(new Disconnected(eventHandler));
    }
}