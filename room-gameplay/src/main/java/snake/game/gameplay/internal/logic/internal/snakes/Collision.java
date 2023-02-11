package snake.game.gameplay.internal.logic.internal.snakes;

import lombok.Value;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;

import java.util.stream.Stream;

interface Collision {
    Stream<PlayerNumber> getCrashedSnakesNumber();

    @Value
    class SnakeHitAnotherSnake implements Collision {
        PlayerNumber hittingSnake;
        PlayerNumber snakeThatGotHit;

        @Override
        public Stream<PlayerNumber> getCrashedSnakesNumber() {
            return Stream.of(hittingSnake);
        }
    }

    @Value
    class SnakeHitItSelf implements Collision {
        PlayerNumber playerNumber;

        @Override
        public Stream<PlayerNumber> getCrashedSnakesNumber() {
            return Stream.of(playerNumber);
        }
    }

    @Value
    class SnakeHitTheWall implements Collision {
        PlayerNumber playerNumber;

        @Override
        public Stream<PlayerNumber> getCrashedSnakesNumber() {
            return Stream.of(playerNumber);
        }
    }

    @Value
    class SnakesClashedHeads implements Collision {
        Stream<PlayerNumber> playerNumbers;

        @Override
        public Stream<PlayerNumber> getCrashedSnakesNumber() {
            return playerNumbers;
        }
    }
}