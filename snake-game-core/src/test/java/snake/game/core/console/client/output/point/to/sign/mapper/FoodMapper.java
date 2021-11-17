package snake.game.core.console.client.output.point.to.sign.mapper;

import snake.game.core.dto.Point;
import io.vavr.control.Option;

class FoodMapper {

    public Option<String> map(Point foodPoint, Point pointToDraw) {
        if (foodPoint.equals(pointToDraw))
            return Option.of(Signs.FOOD);
        else
            return Option.none();
    }
}
