package com.noscompany.snake.game.online.host.room;

public class RoomConfiguration {

    public RoomCreator roomCreator() {
        return new RoomCreatorImpl();
    }
}