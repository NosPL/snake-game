package snake.game.core.console.client.output.point.to.sign.mapper;

import io.vavr.control.Option;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;

class WallMapper {

    public Option<String> map(GridSize gridSize, Point pointToDraw) {
        if (!gridSize.contains(pointToDraw))
            return Option.of(Signs.WALL);
        else
            return Option.none();
    }
}
