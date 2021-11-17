package com.noscompany.snakejavafxclient.game.grid.controller;

import snake.game.core.dto.Direction;
import snake.game.core.dto.SnakeDto;
import snake.game.core.dto.SnakeDto.Body.Part;

import static snake.game.core.dto.Direction.*;

class SnakeToString {

    static String headToString(SnakeDto snakeDto) {
        if (!snakeDto.isAlive())
            return Signs.DEAD_HEAD;
        else if (snakeDto.getHead().isWithFood())
            return Signs.HEAD_WITH_FOOD;
        else {
            Direction direction = snakeDto.getHead().getDirection();
            if (direction == UP)
                return Signs.HEAD_DOWN;
            else if (direction == DOWN)
                return Signs.HEAD_UP;
            else if (direction == LEFT)
                return Signs.HEAD_LEFT;
            else
                return Signs.HEAD_RIGHT;
        }
    }

    static String bodyPartToString(SnakeDto snake, Part part) {
        if (!snake.isAlive())
            return Signs.DEAD_BODY;
        else if (part.isWithFood())
            return Signs.ALIVE_BODY_WITH_FOOD;
        else
            return Signs.ALIVE_BODY;
    }
}