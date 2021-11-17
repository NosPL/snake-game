package snake.game.core.logic.collision.detection;

import lombok.AllArgsConstructor;
import lombok.Value;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.SnakeDto;
import snake.game.core.dto.SnakeNumber;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@AllArgsConstructor
public class CollisionDetector {
    private static final Detector DETECTOR = new Detector.AllDetectors();
    private final GridSize gridSize;

    public Collisions detectCollisions(List<SnakeDto> snakes) {
        final List<Collision> list = DETECTOR
                .apply(snakes, gridSize)
                .collect(toCollection(LinkedList::new));
        return new Collisions(list);
    }

    private interface Detector extends BiFunction<List<SnakeDto>, GridSize, Stream<Collision>> {

        class AllDetectors implements Detector {
            private static final List<Detector> ALL_DETECTORS = List.of(
                    new SnakesClashedHeadsDetector(),
                    new SnakeHitOtherSnakeDetector(),
                    new SnakeHitItselfDetector(),
                    new SnakeHitWallDetector());

            @Override
            public Stream<Collision> apply(List<SnakeDto> snakes, GridSize gridSize) {
                return ALL_DETECTORS
                        .stream()
                        .flatMap(detector -> detector.apply(snakes, gridSize));
            }
        }

        class SnakeHitItselfDetector implements Detector {

            @Override
            public Stream<Collision> apply(List<SnakeDto> snakes, GridSize gridSize) {
                return snakes
                        .stream()
                        .filter(SnakeDto::hitItself)
                        .map(SnakeDto::getSnakeNumber)
                        .map(Collision.SnakeHitItSelf::new);
            }
        }

        class SnakeHitOtherSnakeDetector implements Detector {

            @Override
            public Stream<Collision> apply(List<SnakeDto> snakes, GridSize gridSize) {
                return snakes
                        .stream()
                        .flatMap(snake -> Check.ifThis(snake).hitOneOfOther(snakes));
            }


            @Value(staticConstructor = "ifThis")
            private static class Check {
                SnakeDto hittingSnake;

                Stream<Collision> hitOneOfOther(Collection<SnakeDto> snakes) {
                    return snakes
                            .stream()
                            .filter(snake -> !playerNumberEquals(snake, hittingSnake))
                            .filter(snake -> snake.gotHitBy(hittingSnake))
                            .map(this::toCollision);
                }

                private boolean playerNumberEquals(SnakeDto s1, SnakeDto s2) {
                    return s1.getSnakeNumber().equals(s2.getSnakeNumber());
                }

                private Collision.SnakeHitAnotherSnake toCollision(SnakeDto otherSnake) {
                    return new Collision.SnakeHitAnotherSnake(hittingSnake.getSnakeNumber(), otherSnake.getSnakeNumber());
                }
            }
        }

        class SnakeHitWallDetector implements Detector {

            @Override
            public Stream<Collision> apply(List<SnakeDto> snakes, GridSize gridSize) {
                return snakes
                        .stream()
                        .filter(snake -> snake.wentOutside(gridSize))
                        .map(SnakeDto::getSnakeNumber)
                        .map(Collision.SnakeHitTheWall::new);
            }
        }

        class SnakesClashedHeadsDetector implements Detector {

            @Override
            public Stream<Collision> apply(List<SnakeDto> snakes, GridSize gridSize) {
                return groupHeadsByPoint(snakes)
                        .values()
                        .stream()
                        .filter(list -> list.size() > 1)
                        .map(this::toIds)
                        .map(Collision.SnakesClashedHeads::new);
            }

            private Map<Point, List<SnakeDto.Head>> groupHeadsByPoint(Collection<SnakeDto> snakes) {
                return snakes
                        .stream()
                        .map(SnakeDto::getHead)
                        .collect(groupingBy(SnakeDto.Head::getPoint));
            }

            private List<SnakeNumber> toIds(List<SnakeDto.Head> heads) {
                return heads
                        .stream()
                        .map(SnakeDto.Head::getSnakeNumber)
                        .collect(toList());
            }
        }
    }
}