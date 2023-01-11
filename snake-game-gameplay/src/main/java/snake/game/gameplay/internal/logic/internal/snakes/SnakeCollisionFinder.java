package snake.game.gameplay.internal.logic.internal.snakes;

import lombok.AllArgsConstructor;
import lombok.Value;
import com.noscompany.snake.game.online.contract.messages.game.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.game.dto.Position;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@AllArgsConstructor
class SnakeCollisionFinder {
    private final Detector detector = new Detector.AllDetectors();
    private final GridSize gridSize;

    Set<PlayerNumber> findCrashedSnakesNumbers(Collection<Snake> snakes) {
        return detector
                .apply(snakes, gridSize)
                .flatMap(Collision::getCrashedSnakesNumber)
                .collect(toSet());
    }

    private interface Detector extends BiFunction<Collection<Snake>, GridSize, Stream<Collision>> {

        class AllDetectors implements Detector {
            private final List<Detector> allDetectors = List.of(
                    new SnakesClashedHeadsDetector(),
                    new SnakeHitOtherSnakeDetector(),
                    new SnakeHitItselfDetector(),
                    new SnakeHitWallDetector());

            @Override
            public Stream<Collision> apply(Collection<Snake> snakes, GridSize gridSize) {
                return allDetectors
                        .stream()
                        .flatMap(detector -> detector.apply(snakes, gridSize));
            }
        }

        class SnakeHitItselfDetector implements Detector {

            @Override
            public Stream<Collision> apply(Collection<Snake> snakes, GridSize gridSize) {
                return snakes
                        .stream()
                        .filter(Snake::isAlive)
                        .filter(this::snakeHitItself)
                        .map(Snake::getPlayerNumber)
                        .map(Collision.SnakeHitItSelf::new);
            }

            private boolean snakeHitItself(Snake snake) {
                Position headPosition = snake.getHeadPosition();
                return snake.getBodyPositions().contains(headPosition);
            }
        }

        class SnakeHitOtherSnakeDetector implements Detector {

            @Override
            public Stream<Collision> apply(Collection<Snake> snakes, GridSize gridSize) {
                return snakes
                        .stream()
                        .filter(Snake::isAlive)
                        .flatMap(snake -> Check.ifThis(snake).hitOneOfOther(snakes));
            }


            @Value(staticConstructor = "ifThis")
            private static class Check {
                Snake hittingSnake;

                Stream<Collision> hitOneOfOther(Collection<Snake> snakes) {
                    return snakes
                            .stream()
                            .filter(snake -> !playerNumberEquals(snake, hittingSnake))
                            .filter(this::snakeGotHit)
                            .map(this::toCollision);
                }

                private boolean snakeGotHit(Snake snake) {
                    Position headPosition = hittingSnake.getHeadPosition();
                    return snake.getBodyPositions().contains(headPosition);
                }

                private boolean playerNumberEquals(Snake s1, Snake s2) {
                    return s1.getPlayerNumber().equals(s2.getPlayerNumber());
                }

                private Collision.SnakeHitAnotherSnake toCollision(Snake otherSnake) {
                    return new Collision.SnakeHitAnotherSnake(hittingSnake.getPlayerNumber(), otherSnake.getPlayerNumber());
                }
            }
        }

        class SnakeHitWallDetector implements Detector {

            @Override
            public Stream<Collision> apply(Collection<Snake> snakes, GridSize gridSize) {
                return snakes
                        .stream()
                        .filter(Snake::isAlive)
                        .filter(snake -> snakeWentOutsideGrid(gridSize, snake))
                        .map(Snake::getPlayerNumber)
                        .map(Collision.SnakeHitTheWall::new);
            }

            private boolean snakeWentOutsideGrid(GridSize gridSize, Snake snake) {
                return !gridSize.contains(snake.getHeadPosition());
            }
        }

        class SnakesClashedHeadsDetector implements Detector {

            @Override
            public Stream<Collision> apply(Collection<Snake> snakes, GridSize gridSize) {
                return groupSnakesByHeadPositions(snakes)
                        .values().stream()
                        .filter(list -> list.size() > 1)
                        .map(this::filterAliveSnakes)
                        .map(this::toPlayerNumbers)
                        .map(Collision.SnakesClashedHeads::new);
            }

            private List<Snake> filterAliveSnakes(List<Snake> snakes) {
                return snakes.stream()
                        .filter(Snake::isAlive)
                        .collect(toList());
            }

            private Map<Position, List<Snake>> groupSnakesByHeadPositions(Collection<Snake> snakes) {
                return snakes
                        .stream()
                        .collect(groupingBy(Snake::getHeadPosition));
            }

            private Stream<PlayerNumber> toPlayerNumbers(List<Snake> snakes) {
                return snakes
                        .stream()
                        .map(Snake::getPlayerNumber);
            }
        }
    }
}