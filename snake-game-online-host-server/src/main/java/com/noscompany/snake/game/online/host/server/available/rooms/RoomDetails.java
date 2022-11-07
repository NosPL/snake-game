package com.noscompany.snake.game.online.host.server.available.rooms;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(access = PRIVATE, force = true)
@AllArgsConstructor
public class RoomDetails {
    String name;
    String url;
    long numberOfUsers;

    public RoomDetails setUrl(String url) {
        return new RoomDetails(this.name, url, this.numberOfUsers);
    }
}