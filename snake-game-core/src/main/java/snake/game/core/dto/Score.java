package snake.game.core.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class Score {
    Map<SnakeNumber, Integer> scoreMap;

    public Map<SnakeNumber, Integer> toMap() {
        return new HashMap<>(scoreMap);
    }

    public List<SnakeNumber> getLeaders() {
        Integer highestScore = getHighestScore();
        return scoreMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(highestScore))
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    private Integer getHighestScore() {
        return scoreMap
                .values()
                .stream()
                .mapToInt(i -> i)
                .max()
                .orElse(0);
    }
}