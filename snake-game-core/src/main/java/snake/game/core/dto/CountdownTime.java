package snake.game.core.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class CountdownTime {
    int seconds;

    public int inSeconds() {
        return seconds;
    }

    public static CountdownTime inSeconds(int seconds) {
        return new CountdownTime(Math.max(seconds, 0));
    }
}
