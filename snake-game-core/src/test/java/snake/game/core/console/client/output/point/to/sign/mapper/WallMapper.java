package snake.game.core.console.client.output.point.to.sign.mapper;

import io.vavr.control.Option;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Position;

class WallMapper {

    public Option<String> map(GridSize gridSize, Position positionToDraw) {
        if (!gridSize.contains(positionToDraw))
            return Option.of(Signs.WALL);
        else
            return Option.none();
    }
}
