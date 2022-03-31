package snake.game.core.internal.logic.internal.scoring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import snake.game.core.dto.Score;
import snake.game.core.dto.PlayerNumber;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@AllArgsConstructor
class Snake {
    private final PlayerNumber playerNumber;
    private Integer score;
    private AtomicBoolean alive;

    void addPoints(Integer value) {
        this.score += value;
    }

    void markAsKilled() {
        alive.set(false);
    }

    Score.Snake toDto() {
        return new Score.Snake(playerNumber, alive.get());
    }

    static Snake create(PlayerNumber playerNumber) {
        return new Snake(playerNumber, 0, new AtomicBoolean(true));
    }
}