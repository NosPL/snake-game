package snake.game.core.console.client.output.point.to.sign.mapper;

import io.vavr.control.Option;
import snake.game.core.dto.Position;
import snake.game.core.dto.Snake;

import java.util.Collection;

class SnakeBodyMapper {

    Option<String> map(Collection<Snake> snakes, Position positionToDraw) {
        for (Snake snake : snakes) {
            var result = map(snake, positionToDraw);
            if (result.isDefined())
                return result;
        }
        return Option.none();
    }

    Option<String> map(Snake snake, Position positionToDraw) {
        for (Snake.Node node : snake.getBodyNodes()) {
            if (node.getPosition().equals(positionToDraw))
                return Option.of(toSign(snake, node));
        }
        return Option.none();
    }

    private String toSign(Snake snake, Snake.Node node) {
        if (!snake.isAlive())
            return Signs.KILLED_SNAKE_BODY_PART;
        if (node.isFed())
            return Signs.BODY_PART_WITH_FOOD;
        else
            return Signs.BODY_PART;
    }
}
