package com.noscompany.snakejavafxclient.game.grid.controller;

import com.noscompany.snakejavafxclient.game.SnakesColors;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import lombok.NoArgsConstructor;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.SnakeDto;
import snake.game.core.dto.SnakeNumber;

import java.util.*;

import static java.util.stream.IntStream.range;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class GameGrid extends GridPane {
    private final Map<Point, Cell> cellsMap = new HashMap<>();
    private List<Cell> dirtyCells = new LinkedList<>();

    void update(Collection<SnakeDto> snakes) {
        clear();
        print(snakes);
    }

    void update(Collection<SnakeDto> snakes, Point foodPoint) {
        update(snakes);
        printFoodAt(foodPoint);
    }

    private void clear() {
        dirtyCells.forEach(Cell::clear);
        dirtyCells = new LinkedList<>();
    }

    private void printFoodAt(Point foodPoint) {
        print(foodPoint, GameGridSigns.FOOD, Color.BLACK);
    }

    private void print(Collection<SnakeDto> snakes) {
        for (SnakeDto snake : snakes) {
            SnakeNumber snakeNumber = snake.getSnakeNumber();
            Color color = SnakesColors.get(snakeNumber);
            printHeadOf(snake, color);
            printBodyOf(snake, color);
        }
    }

    private void printBodyOf(SnakeDto snake, Color color) {
        var bodyParts = snake.getBody().getParts();
        for (SnakeDto.Body.Part part : bodyParts) {
            print(part.getPoint(), SnakeToString.bodyPartToString(snake, part), color);
        }
    }

    private void printHeadOf(SnakeDto snake, Color color) {
        print(snake.getHead().getPoint(), SnakeToString.headToString(snake), color);
    }

    private void print(Point point, String string, Color color) {
        var cell = cellsMap.get(point);
        if (cell != null) {
            cell.setText(string);
            cell.setTextFill(color);
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