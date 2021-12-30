package com.noscompany.snakejavafxclient.game.online.client;

import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import lombok.AllArgsConstructor;
import snake.game.core.dto.Direction;

@AllArgsConstructor
public class SnakeMoving {
    private static SnakeOnlineClient snakeOnlineClient;
    private static boolean userInTheRoom = false;
    private static boolean gameIsRunning = false;

    public static void set(SnakeOnlineClient arg) {
        snakeOnlineClient = arg;
    }

    public static void gameIsRunning() {
        gameIsRunning = true;
    }

    public static void gameIsNotRunning() {
        gameIsRunning = false;
    }

    public static void userInTheRoom() {
        userInTheRoom = true;
    }

    public static void userNotInTheRoom() {
        userInTheRoom = false;
    }

    public static void changeSnakeDirection(Direction direction) {
        if (userInTheRoom && gameIsRunning && snakeOnlineClient != null)
            snakeOnlineClient.changeSnakeDirection(direction);
    }
}