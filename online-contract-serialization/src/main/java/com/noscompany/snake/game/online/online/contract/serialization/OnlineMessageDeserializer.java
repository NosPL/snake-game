package com.noscompany.snake.game.online.online.contract.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.noscompany.snake.game.online.contract.messages.ObjectTypeMapper;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.online.contract.serialization.object.type.mappers.*;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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
                    .map(typeMapper -> tryTyMap(messageType, typeMapper))
                    .filter(Option::isDefined)
                    .findAny()
                    .orElse(Option.none())
                    .onEmpty(() -> log.warn("failed to deserialize, type mapper not found"))
                    .map(clazz -> mapToObject(serializedMessage, clazz))
                    .map(message -> (OnlineMessage) message)
                    .peek(message -> log.debug("message successfully deserialized"));
        } catch (Throwable t) {
            log.warn("exception thrown during deserialization, ", t);
            return Option.none();
        }
    }

    private Option<Class<?>> tryTyMap(MessageType messageType, ObjectTypeMapper typeMapper) {
        return typeMapper.mapToObjectType(messageType);
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

    public static OnlineMessageDeserializer instance() {
        var objectMapper = ConfiguredObjectMapperCreator.createInstance();
        return new OnlineMessageDeserializer(objectMapper, allTypeMappers());
    }

    private static List<ObjectTypeMapper> allTypeMappers() {
        return List.of(
                new ChatMessageTypeMapper(),
                new GameOptionsTypeMapper(),
                new GameplayTypeMapper(),
                new IdInitializationTypeMapper(),
                new GameplaySupervisorMessageTypeMapper(),
                new SeatsMessageTypeMapper(),
                new ServerMessageTypeMapper(),
                new UserRegistryMessageTypeMapper());
    }
}