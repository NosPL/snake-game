package snake.game.core.logic.scoreboard;

import lombok.AllArgsConstructor;
import snake.game.core.dto.Score;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.logic.snakes.SnakeConsumedFood;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class ScoreBoard {
    private final Map<SnakeNumber, Integer> snakeScores;

    public static ScoreBoard create(Set<SnakeNumber> snakeNumbers) {
        var map = snakeNumbers
                .stream()
                .collect(toMap(snakeNumber -> snakeNumber, snakeNumber -> 0));
        return new ScoreBoard(map);
    }

    public void update(SnakeConsumedFood event) {
        var snakeNumber = event.getSnakeNumber();
        snakeScores.merge(snakeNumber, 10, Integer::sum);
    }

    public Score getCurrentScore() {
        return new Score(new HashMap<>(snakeScores));
    }
}