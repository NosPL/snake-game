package snake.game.core.dto;

public enum SnakeNumber {
    _1, _2, _3, _4;

    public int toInt() {
        if (this == _1)
            return 1;
        if (this == _2)
            return 2;
        if (this == _3)
            return 3;
        else
            return 4;
    }
}