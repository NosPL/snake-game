package snake.game.core.console.client.output;

import snake.game.core.SnakeGameEventHandler;
import snake.game.core.console.client.output.point.to.sign.mapper.PointToSignMapper;
import snake.game.core.dto.*;
import snake.game.core.events.*;

import java.util.Collection;

public class ConsoleEventHandler implements SnakeGameEventHandler {
    private final PointToSignMapper mapper = new PointToSignMapper();

    public void handle(TimeLeftToGameStartHasChanged timeLeftToGameStartHasChanged) {
        var secondsToStart = timeLeftToGameStartHasChanged.getSecondsLeft();
        System.out.println("game starts in " + secondsToStart + " seconds...");
    }

    public void handle(GameStarted event) {
        System.out.println();
        printGrid(event.getSnakes(), event.getFoodPoint(), event.getGridSize());
        printScore(event.getScore());
    }

    public void handle(GameContinues event) {
        System.out.println();
        printGrid(event.getSnakes(), event.getFoodPoint(), event.getGridSize());
        printScore(event.getScore());
    }

    public void handle(GameFinished event) {
        printGrid(event.getSnakes(), event.getFoodPoint(), event.getGridSize());
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

    public void printGrid(Collection<SnakeDto> snakes, Point foodPoint, GridSize gridSize) {
        var height = gridSize.getHeight();
        var width = gridSize.getWidth();
        for (int y = height; y >= -1; y--) {
            for (int x = -1; x <= width; x++) {
                var sign = mapper
                        .map(Point.point(x, y))
                        .toSignBasedOn(snakes, foodPoint, gridSize);
                System.out.print(sign);
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printScore(Score score) {
        score
                .toMap()
                .forEach(this::printScore);
    }

    private void printScore(SnakeNumber snakeNumber, Integer score) {
        System.out.println("snake " + snakeNumber.toInt() + ". score: " + score + " points");
    }
}