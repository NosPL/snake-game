package snake.game.core.console.client.output.point.to.sign.mapper;

import snake.game.core.dto.Position;
import io.vavr.control.Option;

class FoodMapper {

    public Option<String> map(Option<Position> foodPosition, Position positionToDraw) {
        return foodPosition
                .filter(position -> position.equals(positionToDraw))
                .map(position -> Signs.FOOD);
    }
}
