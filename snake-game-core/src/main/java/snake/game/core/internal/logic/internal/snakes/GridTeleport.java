package snake.game.core.internal.logic.internal.snakes;

import lombok.AllArgsConstructor;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Position;
import snake.game.core.dto.Walls;

@FunctionalInterface
interface GridTeleport {

    static GridTeleport create(GridSize grid, Walls walls) {
        if (walls == Walls.ON)
            return new TeleportDisabled();
        else
            return new TeleportEnabled(grid);
    }

    Position teleport(Position position);

    @AllArgsConstructor
    class TeleportEnabled implements GridTeleport {
        private final GridSize grid;

        @Override
        public Position teleport(Position position) {
            if (wentOverRightWall(position))
                return Position.position(0, position.getY());
            if (wentOverBottomWall(position))
                return Position.position(position.getX(), 0);
            if (wentOverLeftWall(position))
                return Position.position(grid.getWidth() - 1, position.getY());
            if (wentOverTopWall(position))
                return Position.position(position.getX(), grid.getHeight() - 1);
            else
                return position;
        }

        private boolean wentOverTopWall(Position position) {
            return position.getY() < 0;
        }

        private boolean wentOverBottomWall(Position position) {
            return position.getY() >= grid.getHeight();
        }

        private boolean wentOverLeftWall(Position position) {
            return position.getX() < 0;
        }

        private boolean wentOverRightWall(Position position) {
            return position.getX() >= grid.getWidth();
        }

    }

    class TeleportDisabled implements GridTeleport {

        @Override
        public Position teleport(Position position) {
            return position;
        }
    }
}