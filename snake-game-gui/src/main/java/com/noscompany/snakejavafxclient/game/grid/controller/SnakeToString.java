package com.noscompany.snakejavafxclient.game.grid.controller;

import snake.game.core.dto.Direction;
import snake.game.core.dto.SnakeDto;
import snake.game.core.dto.SnakeDto.Body.Part;

import static snake.game.core.dto.Direction.*;

class SnakeToString {

    static String headToString(SnakeDto snakeDto) {
        if (!snakeDto.isAlive())
            return GameGridSigns.DEAD_HEAD;
        else if (snakeDto.getHead().isWithFood())
            return GameGridSigns.HEAD_WITH_FOOD;
        else {
            Direction direction = snakeDto.getHead().getDirection();
            if (direction == UP)
                return GameGridSigns.HEAD_DOWN;
            else if (direction == DOWN)
                return GameGridSigns.HEAD_UP;
            else if (direction == LEFT)
                return GameGridSigns.HEAD_LEFT;
            else
                return GameGridSigns.HEAD_RIGHT;
        }
    }

    static String bodyPartToString(SnakeDto snake, Part part) {
        if (!snake.isAlive())
            return GameGridSigns.DEAD_BODY;
        else if (part.isWithFood())
            return GameGridSigns.ALIVE_BODY_WITH_FOOD;
        else
            return GameGridSigns.ALIVE_BODY;
    }
}