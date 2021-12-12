package snake.game.core.logic.scoring;

import io.vavr.API;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import snake.game.core.dto.Score;
import snake.game.core.dto.SnakeNumber;

import java.util.List;
import java.util.Set;

class ScoreCreator {

    Score create(Map<SnakeNumber, SnakeInfo> snakesInfo) {
        return create(snakesInfo.values());
    }

    Score create(Seq<SnakeInfo> snakesInfo) {
        List<Score.Entry> entries = snakesInfo
                .groupBy(SnakeInfo::getScore)
                .toList()
                .sortBy(Tuple2::_1)
                .reverse()
                .zipWithIndex(this::toScoreEntry)
                .toJavaList();
        return new Score(entries);
    }

    private Score.Entry toScoreEntry(Tuple2<Integer, ? extends Seq<SnakeInfo>> tuple, Integer index) {
        var place = index + 1;
        Integer score = tuple._1();
        Seq<SnakeInfo> snakes = tuple._2();
        return toScoreEntry(place, score, snakes);
    }

    private Score.Entry toScoreEntry(int place, Integer score, Seq<SnakeInfo> snakesInfo) {
        var snakes = snakesInfo
                .map(snakeInfo -> snakeInfo.toScoreSnake(place))
                .sortBy(snakeInfo -> snakeInfo.getSnakeNumber().toInt())
                .toJavaList();
        return new Score.Entry(place, score, snakes);
    }
}