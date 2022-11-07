package snake.game.gameplay.console.client.output.point.to.sign.mapper;

import com.noscompany.snake.game.online.contract.messages.game.dto.Position;
import io.vavr.control.Option;

class FoodMapper {

    public Option<String> map(Option<Position> foodPosition, Position positionToDraw) {
        return foodPosition
                .filter(position -> position.equals(positionToDraw))
                .map(position -> Signs.FOOD);
    }
}
