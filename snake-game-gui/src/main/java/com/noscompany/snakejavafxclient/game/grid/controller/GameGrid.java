package com.noscompany.snakejavafxclient.game.grid.controller;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import lombok.NoArgsConstructor;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.SnakeDto;

import java.util.*;

import static com.noscompany.snakejavafxclient.game.grid.controller.Signs.FOOD;
import static java.util.stream.IntStream.range;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class GameGrid extends GridPane {
    private final Map<Point, Cell> cellsMap = new HashMap<>();
    private List<Cell> dirtyCells = new LinkedList<>();

    void update(Collection<SnakeDto> snakes) {
        update(snakes, Point.point(-1, -1));
    }

    void update(Collection<SnakeDto> snakes, Point foodPoint) {
        dirtyCells.forEach(Cell::clear);
        dirtyCells = new LinkedList<>();
        update(foodPoint, FOOD);
        for (SnakeDto snake : snakes) {
            var bodyParts = snake.getBody().getParts();
            for (SnakeDto.Body.Part part : bodyParts) {
                update(part.getPoint(), SnakeToString.bodyPartToString(snake, part));
            }
            update(snake.getHead().getPoint(), SnakeToString.headToString(snake));
        }
    }

    private void update(Point point, String string) {
        var cell = cellsMap.get(point);
        if (cell != null) {
            cell.setText(string);
            dirtyCells.add(cell);
        }
    }

    private void addColumn(int columnIndex, Cell... cells) {
        super.addColumn(columnIndex, cells);
        for (Cell cell : cells)
            cellsMap.put(cell.getPoint(), cell);
    }

    static class Creator {

        static GameGrid createGrid(GridSize gridSize) {
            var gameGrid = new GameGrid();
            gameGrid.setGridLinesVisible(true);
            var columnSize = gridSize.getHeight();
            var columnCount = gridSize.getWidth();
            for (int i = 0; i < columnCount; i++) {
                var column = column(columnSize);
                gameGrid.addColumn(i, column);
            }
            gameGrid.setPadding(using(gridSize));
            return gameGrid;
        }

        private static Insets using(GridSize gridSize) {
            int padding = calculatePadding(gridSize);
            return new Insets(padding, 0, 0, padding);
        }

        private static int calculatePadding(GridSize gridSize) {
            if (gridSize == GridSize._10x10)
                return 100;
            else if (gridSize == GridSize._15x15)
                return 75;
            else if (gridSize == GridSize._20x20)
                return 50;
            else
                return 0;
        }

        private static Cell[] column(int columnSize) {
            return range(0, columnSize)
                    .mapToObj(i -> Cell.empty())
                    .toArray(Cell[]::new);
        }
    }
}