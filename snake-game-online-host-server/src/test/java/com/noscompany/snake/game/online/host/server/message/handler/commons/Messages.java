package com.noscompany.snake.game.online.host.server.message.handler.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public class Messages {
    private static final ObjectMapper OBJECT_MAPPER = ObjectMapperCreator.createInstance();
    private final List<String> serializedMessages;

    public static Messages empty() {
        return new Messages(new LinkedList<>());
    }

    public Messages addAll(Messages messages) {
        var result = new LinkedList<>(this.serializedMessages);
        result.addAll(messages.toList());
        return new Messages(result);
    }

    public List<String> toList() {
        return new LinkedList<>(serializedMessages);
    }

    public boolean isEmpty() {
        return serializedMessages.isEmpty();
    }

    public boolean contains(OnlineMessage expected) {
        Class<? extends OnlineMessage> clazz = expected.getClass();
        return Vector.ofAll(serializedMessages)
                .flatMap(message -> tryDeserialize(message, clazz))
                .exists(actual -> actual.equals(expected));
    }

    private <T extends OnlineMessage> Option<OnlineMessage> tryDeserialize(String message, Class<T> clazz) {
        try {
            T value = OBJECT_MAPPER.readValue(message, clazz);
            return Option.of(value);
        } catch (JsonProcessingException e) {
            return Option.none();
        }
    }
}