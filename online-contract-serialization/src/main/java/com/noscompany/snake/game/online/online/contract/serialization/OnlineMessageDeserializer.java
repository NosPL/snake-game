package com.noscompany.snake.game.online.online.contract.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

import static com.noscompany.snake.game.online.contract.messages.OnlineMessage.MessageType;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@AllArgsConstructor(access = PRIVATE)
public final class OnlineMessageDeserializer {
    private final ObjectMapper objectMapper;
    private final List<ObjectTypeMapper> objectTypeMappers;

    public Option<OnlineMessage> deserialize(String serializedMessage) {
        try {
            log.debug("received message to deserialize: {}", serializedMessage);
            log.debug("extracting message type...");
            var messageType = getMessageType(serializedMessage);
            log.debug("extracted message type: {}, mapping to object...", messageType);
            return objectTypeMappers
                    .stream()
                    .map(deserializer -> deserializer.mapToObjectType(messageType))
                    .findAny()
                    .orElse(Option.none())
                    .map(clazz -> mapToObject(serializedMessage, clazz))
                    .map(message -> (OnlineMessage) message)
                    .peek(message -> log.debug("message successfully deserialized"))
                    .onEmpty(() -> log.warn("message was not deserialized"));
        } catch (Throwable t) {
            log.warn("exception thrown during deserialization, ", t);
            return Option.none();
        }
    }

    @SneakyThrows
    private Object mapToObject(String serializedMessage, Class<?> clazz) {
        return objectMapper.readValue(serializedMessage, clazz);
    }

    private MessageType getMessageType(String serializedMessage) {
        DocumentContext parsedMessage = JsonPath.parse(serializedMessage);
        String messageTypeName = parsedMessage.read("$.messageType");
        return MessageType.valueOf(messageTypeName);
    }

    public static OnlineMessageDeserializer instance(List<ObjectTypeMapper> objectTypeMappers) {
        objectTypeMappers = new LinkedList<>(objectTypeMappers);
        objectTypeMappers.add(new OnlineContractObjectTypeMapper());
        var objectMapper = ConfiguredObjectMapperCreator.createInstance();
        return new OnlineMessageDeserializer(objectMapper, objectTypeMappers);
    }
}