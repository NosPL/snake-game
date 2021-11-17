package snake.game.core.logic.collision.detection;

import lombok.Value;
import snake.game.core.dto.SnakeNumber;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
public class Collisions {
    List<Collision> list;

    public List<SnakeNumber> getKilledSnakesNumbers() {
        return list
                .stream()
                .flatMap(collision -> collision.getKilledSnakesNumbers().stream())
                .collect(toList());
    }
}