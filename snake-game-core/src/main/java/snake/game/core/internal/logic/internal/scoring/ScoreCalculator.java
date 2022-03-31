package snake.game.core.internal.logic.internal.scoring;

import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.Seq;
import snake.game.core.dto.Score;

import java.util.List;

class ScoreCalculator {

    Score calculateScore(Seq<Snake> snakes) {
        List<Score.Entry> entries = snakes
                .groupBy(Snake::getScore)
                .toList()
                .sortBy(this::score)
                .reverse()
                .zipWithIndex(Tuple2::append)
                .map(this::toScoreEntry)
                .toJavaList();
        return new Score(entries);
    }

    private Integer score(Tuple2<Integer, ? extends Seq<Snake>> snakesByScore) {
        return snakesByScore._1;
    }

    private Score.Entry toScoreEntry(Tuple3<Integer, ? extends Seq<Snake>, Integer> scoreSnakesIndex) {
        int index = scoreSnakesIndex._3();
        int place = index + 1;
        int score = scoreSnakesIndex._1();
        List<Score.Snake> snakes = scoreSnakesIndex._2()
                .map(Snake::toDto)
                .toJavaList();
        return new Score.Entry(place, score, snakes);
    }
}