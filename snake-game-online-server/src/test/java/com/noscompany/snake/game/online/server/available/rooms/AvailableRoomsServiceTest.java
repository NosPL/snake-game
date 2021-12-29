package com.noscompany.snake.game.online.server.available.rooms;

import com.noscompany.snake.game.online.server.available.rooms.commons.BaseTestClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class AvailableRoomsServiceTest extends BaseTestClass {
    private static final String ROOM_PREFIX = "/room/";

    @Test
    public void serviceShouldReturnRoomDetailsOnlyWhenUrlHasCertainPrefix() {
//        given that there are broadcasters with urls:
//        '/room/a'
        var roomAUrl = ROOM_PREFIX + "a";
        addBroadcasterWithId(roomAUrl);
//        '/room/b'
        var roomBUrl = ROOM_PREFIX + "b";
        addBroadcasterWithId(roomBUrl);
//        '/room/c'
        var roomCUrl = ROOM_PREFIX + "c";
        addBroadcasterWithId(roomCUrl);
//        and there is broadcaster without '/room/' prefix
        addBroadcasterWithId("d");
        addBroadcasterWithId("/*");
//        when someone calls for room details list
        Collection<RoomDetails> result = availableRoomsService.getRoomsList();
//        then the result list size should be 3
        Assert.assertEquals(3, result.size());
//        and the result should contain:
        assert result.contains(new RoomDetails("a", roomAUrl, 0));
        assert result.contains(new RoomDetails("b", roomBUrl, 0));
        assert result.contains(new RoomDetails("c", roomCUrl, 0));
    }
}