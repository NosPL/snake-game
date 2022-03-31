package snake.game.core.dto;

import io.vavr.collection.Vector;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class Score {
    List<Entry> entries;

    public Entry getFirstPlace() {
        return entries
                .stream()
                .filter(entry -> entry.getPlace() == 1)
                .findFirst()
                .orElse(new Entry(1, 0, new LinkedList<>()));
    }

    public Set<Snake> getSnakes() {
        return Vector
                .ofAll(entries)
                .flatMap(Entry::getSnakes)
                .toJavaSet();
    }

    @Value
    @NoArgsConstructor(force = true, access = PRIVATE)
    @AllArgsConstructor
    public static class Entry {
        int place;
        int score;
        List<Snake> snakes;
    }

    @Value
    @NoArgsConstructor(force = true, access = PRIVATE)
    @AllArgsConstructor
    public static class Snake {
        PlayerNumber playerNumber;
        boolean alive;
    }
}