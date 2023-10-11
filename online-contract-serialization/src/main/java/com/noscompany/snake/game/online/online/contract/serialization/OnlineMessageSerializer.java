package com.noscompany.snake.game.online.online.contract.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import io.vavr.control.Try;
import io.vavr.jackson.datatype.VavrModule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@AllArgsConstructor(access = PRIVATE)
public final class OnlineMessageSerializer {
    private final ObjectMapper objectMapper;

    public Try<String> serialize(OnlineMessage onlineMessage) {
        return Try
                .of(() -> objectMapper.writeValueAsString(onlineMessage))
                .onFailure(t -> log.warn("Failed to deserialize message.", t));
    }

    public static OnlineMessageSerializer instance() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new VavrModule());
        return new OnlineMessageSerializer(objectMapper);
    }
}