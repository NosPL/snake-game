package com.noscompany.snake.game.commons.messages.commands.decoder;

import com.jayway.jsonpath.JsonPath;
import io.vavr.Tuple;
import io.vavr.Tuple4;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Walls;

@Slf4j
class ChangeGameOptionsMapper {

    public Option<Tuple4<String, GridSize, GameSpeed, Walls>> map(AtmosphereResource r, String msg) {
        try {
            return Option.of(Tuple.of(r.uuid(), gridSize(msg), gameSpeed(msg), walls(msg)));
        } catch (Exception e) {
            log.warn("failed to map message to StartNewGame", e);
            return Option.none();
        }
    }

    private Walls walls(String msg) {
        String string = JsonPath.parse(msg).read("$.walls");
        return Walls.valueOf(string);
    }

    private GameSpeed gameSpeed(String msg) {
        String string = JsonPath.parse(msg).read("$.gameSpeed");
        return GameSpeed.valueOf(string);
    }

    private GridSize gridSize(String msg) {
        String string = JsonPath.parse(msg).read("$.gridSize");
        return GridSize.valueOf(string);
    }
}