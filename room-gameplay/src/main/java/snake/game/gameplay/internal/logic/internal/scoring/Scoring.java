package snake.game.gameplay.internal.logic.internal.scoring;

import io.vavr.collection.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Score;
import snake.game.gameplay.internal.logic.internal.SnakesGotMoved;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
public class Scoring {
    private final Map<PlayerNumber, Snake> snakesMap;
    private final ScoreCalculator scoreCalculator;
    private final int pointsForEatingFood;
    @Getter
    private Score score;

    public void updateScores(SnakesGotMoved snakesGotMoved) {
        snakesGotMoved.getNumbersOfSnakesThatAteFood().forEach(this::addPointsForEatingFood);
        score = scoreCalculator.calculateScore(snakesMap);
    }

    private void addPointsForEatingFood(PlayerNumber playerNumber) {
        snakesMap.get(playerNumber)
                .peek(snake -> snake.addPoints(pointsForEatingFood));
    }
}