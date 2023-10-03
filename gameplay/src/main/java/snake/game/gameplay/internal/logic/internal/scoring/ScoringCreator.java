package snake.game.gameplay.internal.logic.internal.scoring;

import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Score;

import java.util.Set;

public class ScoringCreator {
    private static final int POINTS_TO_ADD_FOR_EATING_FOOD = 10;

    public static Scoring create(Set<PlayerNumber> playerNumbers) {
        ScoreCalculator scoreCalculator = new ScoreCalculator();
        var snakesMap = snakesBy(playerNumbers);
        Score score = scoreCalculator.calculateScore(snakesMap
                .values());
        return new Scoring(
                snakesMap,
                scoreCalculator,
                POINTS_TO_ADD_FOR_EATING_FOOD,
                score);
    }

    private static Map<PlayerNumber, SnakeScore> snakesBy(Set<PlayerNumber> playerNumbers) {
        return Vector.ofAll(playerNumbers)
                .map(SnakeScore::create)
                .toMap(SnakeScore::getPlayerNumber, snakeScore -> snakeScore);
    }
}