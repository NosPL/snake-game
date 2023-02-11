package snake.game.gameplay.console.client.output.point.to.sign.mapper;

import io.vavr.control.Option;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Position;

class WallMapper {

    public Option<String> map(GridSize gridSize, Position positionToDraw) {
        if (!gridSize.contains(positionToDraw))
            return Option.of(Signs.WALL);
        else
            return Option.none();
    }
}
