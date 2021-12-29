package com.noscompany.snake.game.online.server.available.rooms;

import com.noscompany.snake.game.commons.object.mapper.ObjectMapperCreator;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.config.service.DeliverTo;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcasterFactory;

import javax.inject.Inject;

import static com.noscompany.snake.game.online.server.available.rooms.AvailableRoomsWebSocket.URL;
import static org.atmosphere.config.service.DeliverTo.DELIVER_TO.RESOURCE;

@ManagedService(path = URL)
@Slf4j
public class AvailableRoomsWebSocket {
    public static final String URL = "/available-rooms";
    @Inject
    private BroadcasterFactory broadcasterFactory;
    private AvailableRoomsService availableRoomsService;

    @Ready
    @DeliverTo(RESOURCE)
    public void onReady(AtmosphereResource resource) {
        log.info("new resource connected: {}", resource.uuid());
        getAvailableRoomsService()
                .getRoomsListAsString()
                .peek(resource::write);
        Try
                .run(resource::close)
                .onFailure(this::logError);
    }

    private void logError(Throwable throwable) {
        log.warn("failed to close resource", throwable);
    }

    private AvailableRoomsService getAvailableRoomsService() {
        if (availableRoomsService == null) {
            var objectMapper = ObjectMapperCreator.createInstance();
            availableRoomsService = new AvailableRoomsService(broadcasterFactory, objectMapper);
        }
        return availableRoomsService;
    }
}