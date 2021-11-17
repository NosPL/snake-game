package snake.game.core.console.client.output.point.to.sign.mapper;

import io.vavr.control.Option;
import snake.game.core.dto.Point;
import snake.game.core.dto.SnakeDto;

import java.util.Collection;

class SnakeBodyMapper {

    Option<String> map(Collection<SnakeDto> snakes, Point pointToDraw) {
        for (SnakeDto snakeDto : snakes) {
            var result = map(snakeDto, pointToDraw);
            if (result.isDefined())
                return result;
        }
        return Option.none();
    }

    Option<String> map(SnakeDto snakeDto, Point pointToDraw) {
        for (SnakeDto.Body.Part part : snakeDto.getBody().getParts()) {
            if (part.getPoint().equals(pointToDraw))
                return Option.of(toSign(snakeDto, part));
        }
        return Option.none();
    }

    private String toSign(SnakeDto snakeDto, SnakeDto.Body.Part part) {
        if (!snakeDto.isAlive())
            return Signs.KILLED_SNAKE_BODY_PART;
        if (part.isWithFood())
            return Signs.BODY_PART_WITH_FOOD;
        else
            return Signs.BODY_PART;
    }
}
