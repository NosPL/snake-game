package snake.game.core.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameOptions {
    GridSize gridSize;
    GameSpeed gameSpeed;
    Walls walls;
}