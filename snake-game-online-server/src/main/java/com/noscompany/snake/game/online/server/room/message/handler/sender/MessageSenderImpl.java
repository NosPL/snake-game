package com.noscompany.snake.game.online.server.room.message.handler.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.commons.OnlineMessage;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import snake.game.core.events.*;

@AllArgsConstructor
@Slf4j
public class MessageSenderImpl implements MessageSender {
    private final Broadcaster broadcaster;
    private final ObjectMapper objectMapper;

    @Override
    public void send(AtmosphereResource r, OnlineMessage onlineMessage) {
        serialize(onlineMessage).peek(r::write);
    }

    @Override
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
            String string = objectMapper.writeValueAsString(onlineMessage);
            return Option.of(string);
        } catch (JsonProcessingException e) {
            log.warn("failed to serialize message: {}", onlineMessage, e);
            return Option.none();
        }
    }
}