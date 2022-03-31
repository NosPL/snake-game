package snake.game.core.internal.logic.internal;

import lombok.Value;
import snake.game.core.dto.PlayerNumber;
import snake.game.core.dto.Snake;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Value
public class SnakesMoved {
    Collection<Snake> snakes;

    public boolean foodGotConsumed() {
        return !getNumbersOfSnakesThatAteFood().isEmpty();
    }

    public Collection<PlayerNumber> getNumbersOfSnakesThatAteFood() {
        return snakes
                .stream()
                .filter(snake -> snake.getHeadNode().isFed())
                .map(Snake::getPlayerNumber)
                .collect(toList());
    }
}