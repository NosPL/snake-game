package snake.game.gameplay.internal.logic.internal.scoring;

import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.Seq;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Score;

import java.util.List;

class ScoreCalculator {

    Score calculateScore(Seq<SnakeScore> snakeScores) {
        List<Score.Entry> entries = snakeScores
                .groupBy(SnakeScore::getScore)
                .toList()
                .sortBy(this::score)
                .reverse()
                .zipWithIndex(Tuple2::append)
                .map(this::toScoreEntry)
                .toJavaList();
        return new Score(entries);
    }

    private Integer score(Tuple2<Integer, ? extends Seq<SnakeScore>> snakesByScore) {
        return snakesByScore._1;
    }

    private Score.Entry toScoreEntry(Tuple3<Integer, ? extends Seq<SnakeScore>, Integer> scoreSnakesIndex) {
        int score = scoreSnakesIndex._1();
        List<Score.Snake> snakes = toDto(scoreSnakesIndex._2());
        int place = scoreSnakesIndex._3() + 1;
        return new Score.Entry(place, score, snakes);
    }

    private List<Score.Snake> toDto(Seq<SnakeScore> seq) {
        return seq
                .map(SnakeScore::toDto)
                .toJavaList();
    }
}