package snake.game.core.internal.logic.internal.scoring;

import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import lombok.AllArgsConstructor;
import snake.game.core.dto.PlayerNumber;
import snake.game.core.dto.Score;
import snake.game.core.internal.logic.internal.SnakesMoved;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
public class Scoring {
    private final Map<PlayerNumber, Snake> snakesMap;
    private final ScoreCalculator scoreCalculator;
    private final int pointsForEatingFood;
    private Score currentScore;

    public Score getScore() {
        if (currentScore == null)
            currentScore = scoreCalculator.calculateScore(snakes());
        return currentScore;
    }

    public void updateScores(SnakesMoved snakesMoved) {
        snakesMoved.getNumbersOfSnakesThatAteFood().forEach(this::addPointsForEatingFood);
        currentScore = scoreCalculator.calculateScore(snakes());
    }

    private void addPointsForEatingFood(PlayerNumber playerNumber) {
        snakesMap.get(playerNumber)
                .peek(snake -> snake.addPoints(pointsForEatingFood));
    }

    private Seq<Snake> snakes() {
        return snakesMap.values();
    }
}