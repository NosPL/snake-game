package snake.game.gameplay.internal.logic.internal.scoring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Score;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;

@Getter
@AllArgsConstructor
class Snake {
    private final PlayerNumber playerNumber;
    private Integer score;

    void addPoints(Integer value) {
        this.score += value;
    }

    Score.Snake toDto() {
        return new Score.Snake(playerNumber, true);
    }

    static Snake create(PlayerNumber playerNumber) {
        return new Snake(playerNumber, 0);
    }
}