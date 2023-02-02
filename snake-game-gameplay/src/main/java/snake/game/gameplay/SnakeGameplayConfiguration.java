package snake.game.gameplay;

public class SnakeGameplayConfiguration {

    public SnakeGameplayBuilder snakeGameplayBuilder() {
        return new SnakeGameplayBuilderImpl();
    }
}