package com.noscompany.snake.game.online.server.message.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.commons.OnlineMessage;
import io.vavr.control.Option;
import io.vavr.jackson.datatype.VavrModule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import snake.game.core.SnakeGameEventHandler;
import snake.game.core.events.*;

@AllArgsConstructor
@Slf4j
class MessageSender implements SnakeGameEventHandler {
    private final Broadcaster broadcaster;
    private final ObjectMapper objectMapper;

    public void send(AtmosphereResource r, OnlineMessage onlineMessage) {
        serialize(onlineMessage)
                .peek(r::write);
    }

    public void sendToAll(OnlineMessage onlineMessage) {
        serialize(onlineMessage).peek(broadcaster::broadcast);
    }

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        sendToAll(event);
    }

    @Override
    public void handle(GameStarted event) {
        sendToAll(event);
    }

    @Override
    public void handle(GameContinues event) {
        sendToAll(event);
    }

    @Override
    public void handle(GameFinished event) {
        sendToAll(event);
    }

    @Override
    public void handle(GameCancelled event) {
        sendToAll(event);
    }

    @Override
    public void handle(GamePaused event) {
        sendToAll(event);
    }

    @Override
    public void handle(GameResumed event) {
        sendToAll(event);
    }

    private Option<String> serialize(OnlineMessage onlineMessage) {
        try {
            return Option.of(objectMapper.writeValueAsString(onlineMessage));
        } catch (JsonProcessingException e) {
            log.warn("failed to serialize message: {}", onlineMessage, e);
            return Option.none();
        }
    }

    public static MessageSender create(Broadcaster broadcaster) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new VavrModule());
        return new MessageSender(broadcaster, objectMapper);
    }
}