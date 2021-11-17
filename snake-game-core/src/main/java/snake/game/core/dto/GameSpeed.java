package snake.game.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true, access = PRIVATE)
public enum GameSpeed {
    x1(1000),
    x2(500),
    x3(250),
    x4(125),
    x5(62),
    x6(32),
    x7(16),
    x8(8);

    final int pauseTimeInMillis;

    public void pause() {
        try {
            Thread.sleep(pauseTimeInMillis);
        } catch (InterruptedException e) {
        }
    }
}