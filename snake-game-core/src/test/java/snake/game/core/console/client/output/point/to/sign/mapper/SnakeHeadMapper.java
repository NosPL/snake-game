package snake.game.core.console.client.output.point.to.sign.mapper;

import io.vavr.control.Option;
import snake.game.core.dto.Direction;
import snake.game.core.dto.Position;
import snake.game.core.dto.Snake;

import java.util.Collection;

import static snake.game.core.dto.Direction.*;

class SnakeHeadMapper {

    Option<String> map(Collection<Snake> snakes, Position positionToDraw) {
        for (Snake snake : snakes) {
            var result = map(snake, positionToDraw);
            if (result.isDefined())
                return result;
        }
        return Option.none();
    }

    Option<String> map(Snake snake, Position positionToDraw) {
        var head = snake.getHeadNode();
        var headPoint = head.getPosition();
        if (headPoint.equals(positionToDraw)) {
            if (!snake.isAlive())
                return Option.of(Signs.KILLED_SNAKE_HEAD);
            else if (head.isFed())
                return Option.of(Signs.HEAD_WITH_FOOD);
            else
                return Option.of(mapToSign(snake.getDirection()));
        } else
            return Option.none();
    }

    private String mapToSign(Direction direction) {
        if (direction == UP)
            return Signs.HEAD_UP;
        if (direction == DOWN)
            return Signs.HEAD_DOWN;
        if (direction == LEFT)
            return Signs.HEAD_LEFT;
        else
            return Signs.HEAD_RIGHT;
    }
}
