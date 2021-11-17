package snake.game.core.logic.snakes;

import lombok.AllArgsConstructor;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.Walls;

interface GridTeleport {

    static GridTeleport create(GridSize grid, Walls walls) {
        if (walls == Walls.ON)
            return new TeleportDisabled();
        else
            return new TeleportEnabled(grid);
    }

    Point teleportPointIfItWentOutsideGrid(Point point);

    @AllArgsConstructor
    class TeleportEnabled implements GridTeleport {
        private final GridSize grid;

        @Override
        public Point teleportPointIfItWentOutsideGrid(Point point) {
            if (wentOverRightWall(point))
                return Point.point(0, point.getY());
            if (wentOverBottomWall(point))
                return Point.point(point.getX(), 0);
            if (wentOverLeftWall(point))
                return Point.point(grid.getWidth() - 1, point.getY());
            if (wentOverTopWall(point))
                return Point.point(point.getX(), grid.getHeight() - 1);
            else
                return point;
        }

        private boolean wentOverTopWall(Point point) {
            return point.getY() < 0;
        }

        private boolean wentOverBottomWall(Point point) {
            return point.getY() >= grid.getHeight();
        }

        private boolean wentOverLeftWall(Point point) {
            return point.getX() < 0;
        }

        private boolean wentOverRightWall(Point point) {
            return point.getX() >= grid.getWidth();
        }

    }

    class TeleportDisabled implements GridTeleport {

        @Override
        public Point teleportPointIfItWentOutsideGrid(Point point) {
            return point;
        }
    }
}