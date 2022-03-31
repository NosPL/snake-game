package snake.game.core.internal.logic.internal.scoring;

import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import snake.game.core.dto.PlayerNumber;

import java.util.Set;

public class ScoringCreator {
    private static final int POINTS_TO_ADD_FOR_EATING_FOOD = 10;

    public static Scoring create(Set<PlayerNumber> playerNumbers) {
        ScoreCalculator scoreCalculator = new ScoreCalculator();
        var snakesMap = snakesBy(playerNumbers);
        return new Scoring(
                snakesMap,
                scoreCalculator,
                POINTS_TO_ADD_FOR_EATING_FOOD,
                null);
    }

    private static Map<PlayerNumber, Snake> snakesBy(Set<PlayerNumber> playerNumbers) {
        return Vector.ofAll(playerNumbers)
                .map(Snake::create)
                .toMap(Snake::getPlayerNumber, snake -> snake);
    }
}