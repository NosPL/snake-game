package com.noscompany.snakejavafxclient.components.commons.game.grid;

import io.vavr.control.Option;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import lombok.RequiredArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Position;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;

import java.util.*;

import static com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
class GameGrid extends GridPane {
    private final Map<Position, Cell> cellsMap = new HashMap<>();
    private List<Cell> dirtyCells = new LinkedList<>();
    private final SnakeMapper snakeMapper = new SnakeMapper();
    private final GridSize gridSize;
    private final Walls walls;

    void update(Collection<Snake> snakes) {
        clear();
        print(snakes);
    }

    void update(Collection<Snake> snakes, Option<Position> foodPosition) {
        clear();
        print(snakes);
        foodPosition.peek(this::printFood);
    }

    private void clear() {
        dirtyCells.forEach(Cell::clear);
        dirtyCells = new LinkedList<>();
    }

    private void printFood(Position position) {
        print(position, GameGridSigns.FOOD, Color.BLACK);
    }

    private void print(Collection<Snake> snakes) {
        List<PrintableSnake> printableSnakes = snakeMapper.map(snakes);
        printableSnakes.forEach(this::printBody);
        printableSnakes.forEach(this::printHead);
    }

    private void printBody(PrintableSnake printableSnake) {
        printableSnake.getBodyNodes().forEach(this::print);
    }

    private void printHead(PrintableSnake printableSnake) {
        print(printableSnake.getHeadNode());
    }

    private void print(PrintableSnake.Node node) {
        print(node.getPosition(), node.getString(), node.getColor());
    }

    private void print(Position position, String string, Color color) {
        position = wallsOffset(position);
        var cell = cellsMap.get(position);
        if (cell != null) {
            cell.setText(string);
            cell.setTextFill(color);
            dirtyCells.add(cell);
        }
    }

    private Position wallsOffset(Position position) {
        if (walls == Walls.OFF)
            return position;
        return position
                .moveUpBy(1)
                .moveRightBy(1);
    }

    private void addColumn(int columnIndex, Cell... cells) {
        super.addColumn(columnIndex, cells);
        for (Cell cell : cells)
            cellsMap.put(cell.getPosition(), cell);
    }

    private void paintBorderCells(Color color) {
        getBorderCells()
                .forEach(cell -> cell.setBackground(color));
    }

    private List<Cell> getBorderCells() {
        return cellsMap
                .values().stream()
                .filter(this::isBorderCell)
                .collect(toList());
    }

    private boolean isBorderCell(Cell cell) {
        var x = cell.getPosition().getX();
        var y = cell.getPosition().getY();
        return isBorderCoordinate(x) || isBorderCoordinate(y);
    }

    private boolean isBorderCoordinate(int coordinate) {
        var maxCoordinate = getMaxCoordinate();
        return coordinate == 0 || coordinate == maxCoordinate;
    }

    private int getMaxCoordinate() {
        if (walls == Walls.OFF)
            return gridSize.getHeight();
        else
            return gridSize.getHeight() + 1;
    }

    static class Creator {

        static GameGrid createGrid(GridSize gridSize, Walls walls) {
            var gameGrid = new GameGrid(gridSize, walls);
            gameGrid.setGridLinesVisible(true);
            addColumns(gameGrid, gridSize, walls);
            if (walls == Walls.ON)
                gameGrid.paintBorderCells(Color.BLACK);
            return gameGrid;
        }

        private static void addColumns(GameGrid gameGrid, GridSize gridSize, Walls walls) {
            var columnSize = getColumnSize(gridSize, walls);
            var columnCount = getColumnCount(gridSize, walls);
            int cellSize = fontSizeFor(gridSize);
            for (int i = 0; i < columnCount; i++) {
                var column = column(columnSize, cellSize);
                gameGrid.addColumn(i, column);
            }
        }

        private static int getColumnCount(GridSize gridSize, Walls walls) {
            return walls == Walls.OFF ? gridSize.getWidth() : gridSize.getWidth() + 2;
        }

        private static int getColumnSize(GridSize gridSize, Walls walls) {
            return walls == Walls.OFF ? gridSize.getHeight() : gridSize.getHeight() + 2;
        }

        private static int fontSizeFor(GridSize gridSize) {
            if (gridSize == _10x10)
                return 25;
            if (gridSize == _15x15)
                return 20;
            if (gridSize == _20x20)
                return 14;
            return 11;
        }

        private static Cell[] column(int columnSize, int cellSize) {
            return range(0, columnSize)
                    .mapToObj(i -> Cell.ofSize(cellSize))
                    .toArray(Cell[]::new);
        }
    }
}