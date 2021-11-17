package server.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.server.lobby.GameStateDto;
import lombok.SneakyThrows;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.SnakeGameEventHandler;
import snake.game.core.dto.GameState;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.events.*;

import java.util.Set;

import static com.noscompany.snake.game.server.lobby.GameStateDto.asDto;

class SerializationTest {

    @SneakyThrows
    public static void main(String[] args) {
        var objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(asDto(sampleGameState()));
        var point = objectMapper.readValue(json, GameStateDto.class);
        System.out.println(point);
    }

    private static GameState sampleGameState() {
        return new SnakeGameConfiguration()
                .set(new NullObjectHandler())
                .set(Set.of(SnakeNumber.values()))
                .create()
                .get()
                .getGameState();
    }

    private static class NullObjectHandler implements SnakeGameEventHandler {

        @Override
        public void handle(TimeLeftToGameStartHasChanged event) {

        }

        @Override
        public void handle(GameStarted event) {

        }

        @Override
        public void handle(GameContinues event) {

        }

        @Override
        public void handle(GameFinished event) {

        }

        @Override
        public void handle(GameCancelled event) {

        }

        @Override
        public void handle(GamePaused event) {

        }

        @Override
        public void handle(GameResumed event) {

        }
    }
}