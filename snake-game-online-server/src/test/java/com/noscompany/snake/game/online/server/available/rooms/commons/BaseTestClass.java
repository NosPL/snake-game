package com.noscompany.snake.game.online.server.available.rooms.commons;

import com.noscompany.snake.game.commons.object.mapper.ObjectMapperCreator;
import com.noscompany.snake.game.online.server.available.rooms.AvailableRoomsService;
import org.junit.Before;


public class BaseTestClass {
    protected AvailableRoomsService availableRoomsService;
    private BroadcasterFactoryMock broadcasterFactoryMock;

    @Before
    public void setup() {
        broadcasterFactoryMock = new BroadcasterFactoryMock();
        availableRoomsService = new AvailableRoomsService(broadcasterFactoryMock, ObjectMapperCreator.createInstance());
    }

    protected void addBroadcasterWithId(String id) {
        broadcasterFactoryMock.add(new BroadcasterMock(id));
    }
}
