package snake.game.core.logic.scoring;

import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import snake.game.core.dto.SnakeNumber;

import java.util.Set;

public class ScoringSystemCreator {
    private static final int POINTS_TO_ADD_FOR_EATING_FOOD = 10;

    public static ScoringSystem create(Set<SnakeNumber> snakeNumbers) {
        ScoreCreator scoreCreator = new ScoreCreator();
        var snakes = snakesBy(snakeNumbers);
        return new ScoringSystem(
                snakes,
                scoreCreator,
                POINTS_TO_ADD_FOR_EATING_FOOD);
    }

    private static Map<SnakeNumber, SnakeInfo> snakesBy(Set<SnakeNumber> snakeNumbers) {
        return Vector.ofAll(snakeNumbers)
                .map(SnakeInfo::create)
                .toMap(SnakeInfo::getSnakeNumber, snakeInfo -> snakeInfo);
    }
}