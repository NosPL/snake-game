package snake.game.gameplay;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import io.vavr.control.Either;
import lombok.NonNull;

import java.util.Collection;

public interface SnakeGameplayBuilder {
    SnakeGameplayBuilder set(@NonNull PlayerNumber player);

    SnakeGameplayBuilder set(@NonNull Collection<PlayerNumber> players);

    SnakeGameplayBuilder set(@NonNull GameSpeed gameSpeed);

    SnakeGameplayBuilder set(@NonNull GridSize gridSize);

    SnakeGameplayBuilder set(@NonNull Walls walls);

    SnakeGameplayBuilder set(@NonNull CountdownTime countdownTime);

    SnakeGameplayBuilder set(@NonNull SnakeGameEventHandler eventHandler);

    Either<Error, SnakeGameplay> createGame();

    enum Error {
        PLAYERS_ARE_NOT_SET;
    }
}
