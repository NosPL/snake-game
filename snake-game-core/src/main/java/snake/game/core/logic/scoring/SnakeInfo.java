package snake.game.core.logic.scoring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import snake.game.core.dto.Score;
import snake.game.core.dto.SnakeNumber;

@Getter
@AllArgsConstructor
class SnakeInfo {
    private final SnakeNumber snakeNumber;
    private Integer score;
    private boolean alive;

    void addToScore(Integer value) {
        this.score += value;
    }

    void markAsKilled() {
        this.alive = false;
    }

    Score.Snake toScoreSnake(int place) {
        return new Score.Snake(snakeNumber, place, score, alive);
    }

    static SnakeInfo create(SnakeNumber snakeNumber) {
        return new SnakeInfo(snakeNumber, 0, true);
    }
}
