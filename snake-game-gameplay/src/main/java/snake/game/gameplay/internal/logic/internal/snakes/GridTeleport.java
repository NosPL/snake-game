package snake.game.gameplay.internal.logic.internal.snakes;

import lombok.AllArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.game.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.game.dto.Position;
import com.noscompany.snake.game.online.contract.messages.game.dto.Walls;

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
        private final GridSize gridSize;

        @Override
        public Position teleport(Position position) {
            if (wentOverRightWall(position))
                return Position.position(0, position.getY());
            if (wentOverBottomWall(position))
                return Position.position(position.getX(), 0);
            if (wentOverLeftWall(position))
                return Position.position(gridSize.getWidth() - 1, position.getY());
            if (wentOverTopWall(position))
                return Position.position(position.getX(), gridSize.getHeight() - 1);
            else
                return position;
        }

        private boolean wentOverTopWall(Position position) {
            return position.getY() < 0;
        }

        private boolean wentOverBottomWall(Position position) {
            return position.getY() >= gridSize.getHeight();
        }

        private boolean wentOverLeftWall(Position position) {
            return position.getX() < 0;
        }

        private boolean wentOverRightWall(Position position) {
            return position.getX() >= gridSize.getWidth();
        }

    }

    class TeleportDisabled implements GridTeleport {

        @Override
        public Position teleport(Position position) {
            return position;
        }
    }
}