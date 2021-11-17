package snake.game.core.console.client.output.point.to.sign.mapper;

import lombok.AllArgsConstructor;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.SnakeDto;

import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

public class PointToSignMapper {
    private final WallMapper wallMapper = new WallMapper();
    private final FoodMapper foodMapper = new FoodMapper();
    private final SnakeHeadMapper snakeHeadMapper = new SnakeHeadMapper();
    private final SnakeBodyMapper snakeBodyMapper = new SnakeBodyMapper();

    private String map(Collection<SnakeDto> snakes,
                       Point foodPoint,
                       GridSize gridSize,
                       Point pointToDraw) {
        return snakeHeadMapper
                .map(snakes, pointToDraw)
                .orElse(snakeBodyMapper.map(snakes, pointToDraw))
                .orElse(wallMapper.map(gridSize, pointToDraw))
                .orElse(foodMapper.map(foodPoint, pointToDraw))
                .getOrElse(Signs.EMPTY_POINT);
    }

    public CreateSignFor map(Point point) {
        return new CreateSignFor(this, point);
    }

    @AllArgsConstructor(access = PRIVATE)
    public static class CreateSignFor {
        private PointToSignMapper mapper;
        private Point pointToDraw;

        public String toSignBasedOn(Collection<SnakeDto> snakes, Point foodPoint, GridSize gridSize) {
            return mapper.map(snakes, foodPoint, gridSize, pointToDraw);
        }
    }
}