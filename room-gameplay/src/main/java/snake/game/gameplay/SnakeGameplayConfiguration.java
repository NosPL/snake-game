package snake.game.gameplay;

public class SnakeGameplayConfiguration {

    public SnakeGameplayCreator snakeGameplayCreator() {
        return new SnakeGameplayCreatorImpl();
    }
}