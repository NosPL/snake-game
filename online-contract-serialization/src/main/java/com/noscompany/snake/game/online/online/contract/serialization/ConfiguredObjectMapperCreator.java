package com.noscompany.snake.game.online.online.contract.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.jackson.datatype.VavrModule;

public class ConfiguredObjectMapperCreator {

    public static ObjectMapper createInstance() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.registerModule(new VavrModule());
    }
}