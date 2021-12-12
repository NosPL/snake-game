package snake.game.core.logic.scoring;

import io.vavr.collection.Map;
import lombok.AllArgsConstructor;
import snake.game.core.dto.Score;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.logic.collision.detection.Collisions;
import snake.game.core.logic.snakes.SnakeConsumedFood;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
public class ScoringSystem {
    private final Map<SnakeNumber, SnakeInfo> snakes;
    private final ScoreCreator scoreCreator;
    private final int pointsForEatingFood;

    public void handle(SnakeConsumedFood event) {
        SnakeNumber snakeNumber = event.getSnakeNumber();
        snakes.get(snakeNumber)
                .peek(snakeInfo -> snakeInfo.addToScore(pointsForEatingFood));
    }

    public void handle(Collisions collisions) {
        collisions
                .getKilledSnakesNumbers()
                .forEach(this::markAsKilled);
    }

    private void markAsKilled(SnakeNumber snakeNumber) {
        snakes.get(snakeNumber)
                .peek(SnakeInfo::markAsKilled);
    }

    public Score getCurrentScore() {
        return scoreCreator.create(snakes);
    }
}