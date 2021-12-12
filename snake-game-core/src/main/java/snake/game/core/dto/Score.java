package snake.game.core.dto;

import io.vavr.collection.Vector;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class Score {
    List<Entry> entries;

    public Set<Snake> getSnakes() {
        return Vector.ofAll(entries)
                .flatMap(Entry::getSnakes)
                .toJavaSet();
    }

    @Value
    public static class Entry {
        int place;
        int score;
        List<Snake> snakes;
    }

    @Value
    public static class Snake {
        SnakeNumber snakeNumber;
        int place;
        int score;
        boolean alive;
    }
}