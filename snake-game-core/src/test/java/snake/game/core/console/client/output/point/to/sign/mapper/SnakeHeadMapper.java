package snake.game.core.console.client.output.point.to.sign.mapper;

import io.vavr.control.Option;
import snake.game.core.dto.Direction;
import snake.game.core.dto.Point;
import snake.game.core.dto.SnakeDto;

import java.util.Collection;

import static snake.game.core.dto.Direction.*;

class SnakeHeadMapper {

    Option<String> map(Collection<SnakeDto> snakes, Point pointToDraw) {
        for (SnakeDto snakeDto : snakes) {
            var result = map(snakeDto, pointToDraw);
            if (result.isDefined())
                return result;
        }
        return Option.none();
    }

    Option<String> map(SnakeDto snakeDto, Point pointToDraw) {
        var head = snakeDto.getHead();
        var headPoint = head.getPoint();
        if (headPoint.equals(pointToDraw)) {
            if (!snakeDto.isAlive())
                return Option.of(Signs.KILLED_SNAKE_HEAD);
            else if (head.isWithFood())
                return Option.of(Signs.HEAD_WITH_FOOD);
            else
                return Option.of(mapToSign(head.getDirection()));
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
