package com.noscompany.snake.game.online.contract.object.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.vavr.jackson.datatype.VavrModule;

public class ObjectMapperCreator {

    public static ObjectMapper createInstance() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.registerModule(new VavrModule());
    }
}