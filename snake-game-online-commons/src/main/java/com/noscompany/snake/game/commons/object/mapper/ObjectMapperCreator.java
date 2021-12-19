package com.noscompany.snake.game.commons.object.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.jackson.datatype.VavrModule;

public class ObjectMapperCreator {

    public static ObjectMapper createInstance() {
        return new ObjectMapper().registerModule(new VavrModule());
    }
}
