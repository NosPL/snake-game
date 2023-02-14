package snake.game.gameplay.console.client.output;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import io.vavr.control.Option;
import snake.game.gameplay.GameplayEventHandler;
import snake.game.gameplay.console.client.output.point.to.sign.mapper.PointToSignMapper;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Position;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Score;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake;

import java.util.Collection;

public class ConsolePrinter implements GameplayEventHandler {
    private final PointToSignMapper mapper = new PointToSignMapper();

    public void handle(TimeLeftToGameStartHasChanged timeLeftToGameStartHasChanged) {
        var secondsToStart = timeLeftToGameStartHasChanged.getSecondsLeft();
        System.out.println("game starts in " + secondsToStart + " seconds...");
    }

    public void handle(GameStarted event) {
        System.out.println();
        printGrid(event.getSnakes(), event.getFoodPosition(), event.getGridSize());
        printScore(event.getScore());
    }

    public void handle(SnakesMoved event) {
        System.out.println();
        printGrid(event.getSnakes(), event.getFoodPosition(), event.getGridSize());
        printScore(event.getScore());
    }

    public void handle(GameFinished event) {
        printGrid(event.getSnakes(), event.getFoodPosition(), event.getGridSize());
        printScore(event.getScore());
        System.out.println();
        System.out.println();
        System.out.println("GAME FINISHED");
        System.out.print("Press ENTER to close...");
    }

    public void handle(GameCancelled event) {
        System.out.println("GAME CANCELLED");
        System.out.print("Press ENTER to close...");
    }

    @Override
    public void handle(GamePaused event) {
        System.out.println("GAME PAUSED");
    }

    @Override
    public void handle(GameResumed event) {
        System.out.println("GAME RESUMED");
    }

    public void printGrid(Collection<Snake> snakes, Option<Position> foodPosition, GridSize gridSize) {
        var height = gridSize.getHeight();
        var width = gridSize.getWidth();
        for (int y = height; y >= -1; y--) {
            for (int x = -1; x <= width; x++) {
                var sign = mapper
                        .map(Position.position(x, y))
                        .toSignBasedOn(snakes, foodPosition, gridSize);
                System.out.print(sign);
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printScore(Score score) {
        score
                .getEntries().stream()
                .map(this::toString)
                .forEach(System.out::println);
    }

    private String toString(Score.Entry entry) {
        int place = entry.getPlace();
        int score = entry.getScore();
        return place + ". score: " + score + " points";
    }
}