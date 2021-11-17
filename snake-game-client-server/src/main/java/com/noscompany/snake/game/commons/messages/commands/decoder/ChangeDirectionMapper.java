package com.noscompany.snake.game.commons.messages.commands.decoder;

import com.jayway.jsonpath.JsonPath;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;
import snake.game.core.dto.Direction;

@Slf4j
class ChangeDirectionMapper {

    public Option<Tuple2<String, Direction>> map(AtmosphereResource r, String msg) {
        try {
            return Option.of(Tuple.of(r.uuid(), direction(msg)));
        } catch (Exception e) {
            log.warn("failed to map message to ChangeSnakeDirection", e);
            return Option.none();
        }
    }

    private Direction direction(String msg) {
        String string = JsonPath
                .parse(msg)
                .read("$.direction");
        return Direction.valueOf(string);
    }
}