package snake.game.gameplay.internal.logic.internal.scoring;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake;
import io.vavr.collection.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Score;

import java.util.List;
import java.util.stream.Stream;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
public class Scoring {
    private final Map<PlayerNumber, SnakeScore> snakesMap;
    private final ScoreCalculator scoreCalculator;
    private final int pointsForEatingFood;
    @Getter
    private Score score;

    public void updateScores(List<Snake> snakes) {
        getFedSnakesNumbers(snakes).forEach(this::addPointsForEatingFood);
        score = scoreCalculator.calculateScore(snakesMap.values());
    }

    private Stream<PlayerNumber> getFedSnakesNumbers(List<Snake> snakes) {
        return snakes.stream()
                .filter(Snake::isAlive)
                .filter(snake -> snake.getHeadNode().isFed())
                .map(Snake::getPlayerNumber);
    }

    private void addPointsForEatingFood(PlayerNumber playerNumber) {
        snakesMap.get(playerNumber)
                .peek(snakeScore -> snakeScore.addPoints(pointsForEatingFood));
    }
}