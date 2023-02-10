package snake.game.gameplay.console.client.output.point.to.sign.mapper;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Position;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake;

import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

public class PointToSignMapper {
    private final WallMapper wallMapper = new WallMapper();
    private final FoodMapper foodMapper = new FoodMapper();
    private final SnakeHeadMapper snakeHeadMapper = new SnakeHeadMapper();
    private final SnakeBodyMapper snakeBodyMapper = new SnakeBodyMapper();

    private String map(Collection<Snake> snakes,
                       Option<Position> foodPosition,
                       GridSize gridSize,
                       Position positionToDraw) {
        return snakeHeadMapper
                .map(snakes, positionToDraw)
                .orElse(snakeBodyMapper.map(snakes, positionToDraw))
                .orElse(wallMapper.map(gridSize, positionToDraw))
                .orElse(foodMapper.map(foodPosition, positionToDraw))
                .getOrElse(Signs.EMPTY_POINT);
    }

    public CreateSignFor map(Position position) {
        return new CreateSignFor(this, position);
    }

    @AllArgsConstructor(access = PRIVATE)
    public static class CreateSignFor {
        private PointToSignMapper mapper;
        private Position positionToDraw;

        public String toSignBasedOn(Collection<Snake> snakes, Option<Position> foodPosition, GridSize gridSize) {
            return mapper.map(snakes, foodPosition, gridSize, positionToDraw);
        }
    }
}