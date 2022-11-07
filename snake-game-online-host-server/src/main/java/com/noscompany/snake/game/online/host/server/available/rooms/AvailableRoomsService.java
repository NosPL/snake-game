package com.noscompany.snake.game.online.host.server.available.rooms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.host.server.websocket.SnakeGameRoomWebSocket;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;

import java.util.Collection;

import static io.vavr.collection.Vector.ofAll;

@Slf4j
@AllArgsConstructor
public class AvailableRoomsService {
    private final BroadcasterFactory broadcasterFactory;
    private final ObjectMapper objectMapper;

    public Option<String> getRoomsListAsString() {
        return serialize(getRoomsList());
    }


    private Option<String> serialize(Collection<RoomDetails> rooms) {
        try {
            return Option.of(objectMapper.writeValueAsString(rooms));
        } catch (JsonProcessingException e) {
            log.error("failed to serialize rooms list: {}", rooms, e);
            return Option.none();
        }
    }

    public Collection<RoomDetails> getRoomsList() {
        log.info("fetching available rooms");
        return ofAll(broadcasters())
                .filter(this::filterById)
                .flatMap(this::toRoom)
                .toJavaSet();
    }

    private Collection<Broadcaster> broadcasters() {
        return broadcasterFactory.lookupAll();
    }

    private boolean filterById(Broadcaster broadcaster) {
        return !broadcaster.getID().equals("/*") &&
                !broadcaster.getID().equals("/available-rooms") &&
                broadcaster.getID().startsWith(SnakeGameRoomWebSocket.URL_PREFIX) &&
                !broadcaster.getID().contains("[") &&
                !broadcaster.isDestroyed();
    }

    private Option<RoomDetails> toRoom(Broadcaster broadcaster) {
        String roomAddress = broadcaster.getID();
        int numberOfUsers = broadcaster.getAtmosphereResources().size();
        return nameOf(broadcaster)
                .map(name -> new RoomDetails(name, roomAddress, numberOfUsers));
    }

    private Option<String> nameOf(Broadcaster broadcaster) {
        String[] split = broadcaster.getID().split("/");
        if (split.length == 0)
            return Option.none();
        else
            return Option.of(split[split.length - 1]);
    }
}