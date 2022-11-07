package com.noscompany.snake.game.online.host.room.message.dispatcher;

import com.noscompany.snake.game.online.host.room.Room;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.Server;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

class RoomCommandHandlerForHostTest {
    private RoomCommandHandlerForHost commandHandlerForHost;
    private TestServer testServer;
    private TestRoomEventHandlerForHost eventHandlerForHost;

    @Before
    void init() {
        eventHandlerForHost = TestRoomEventHandlerForHost.create();
        testServer = TestServer.create();
        commandHandlerForHost = new RoomMessageDispatcherConfiguration().roomCommandHandlerForHost(randomHostName(), serverMock(), eventHandlerForHost);
    }

    private String randomHostName() {
        return UUID.randomUUID().toString();
    }

    private Room roomMock() {
        return null;
    }

    private Server serverMock() {
        return null;
    }

    @Test
    public void userSentChatMessageShouldBeSentToHostAndAllRemoteClients() {

    }
}