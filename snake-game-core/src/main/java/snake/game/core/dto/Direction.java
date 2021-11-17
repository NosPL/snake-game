package snake.game.core.dto;

public enum Direction {
    UP, DOWN, RIGHT, LEFT;

    public Direction getOpposite() {
        if (this == UP)
            return DOWN;
        if (this == DOWN)
            return UP;
        if (this == RIGHT)
            return LEFT;
        else
            return RIGHT;
    }
}
