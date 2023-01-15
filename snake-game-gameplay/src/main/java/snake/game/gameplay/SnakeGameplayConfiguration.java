package snake.game.gameplay;

import dagger.Module;
import dagger.Provides;

@Module
public class SnakeGameplayConfiguration {

    @Provides
    public SnakeGameplayBuilder snakeGameplayBuilder() {
        return new SnakeGameplayBuilderImpl();
    }
}