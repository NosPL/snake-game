package com.noscompany.snake.game.commons.messages.commands.decoder;

import com.jayway.jsonpath.JsonPath;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;
import snake.game.core.dto.SnakeNumber;

@Slf4j
class JoinGameMapper {

    public Option<Tuple2<String, SnakeNumber>> map(AtmosphereResource r, String msg) {
        try {
            return Option.of(Tuple.of(r.uuid(), snakeNumber(msg)));
        } catch (Exception e) {
            log.warn("failed to map message to ChangeSnakeDirection", e);
            return Option.none();
        }
    }

    private SnakeNumber snakeNumber(String msg) {
        String string = JsonPath
                .parse(msg)
                .read("$.playerNumber");
        return SnakeNumber.valueOf(string);
    }
}