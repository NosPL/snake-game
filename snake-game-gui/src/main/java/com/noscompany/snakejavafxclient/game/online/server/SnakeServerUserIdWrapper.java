package com.noscompany.snakejavafxclient.game.online.server;

import com.noscompany.snake.game.server.local.api.SnakeServer;
import lombok.AllArgsConstructor;
import snake.game.core.dto.*;

@AllArgsConstructor
public class SnakeServerUserIdWrapper {
    private final String userId;
    private final SnakeServer snakeServer;

    public void addUser() {
        snakeServer.addUser(userId);
    }

    public void removeUser() {
        snakeServer.removeUser(userId);
    }


    public void startServer(String ipv4Address, String port) {
        snakeServer.startServer(ipv4Address, port);
    }


    public void takeASeat(SnakeNumber snakeNumberNumber) {
        snakeServer.takeASeat(userId, snakeNumberNumber);
    }


    public void freeUpASeat() {
        snakeServer.freeUpASeat(userId);
    }


    public void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        snakeServer.changeGameOptions(userId, gridSize, gameSpeed, walls);
    }


    public void startGame() {
        snakeServer.startGame(userId);
    }


    public void changeSnakeDirection(Direction direction) {
        snakeServer.changeSnakeDirection(userId, direction);
    }


    public void pauseGame() {
        snakeServer.pauseGame(userId);
    }


    public void resumeGame() {
        snakeServer.resumeGame(userId);
    }


    public void cancelGame() {
        snakeServer.cancelGame(userId);
    }


    public void sendChatMessage(String messageContent) {
        snakeServer.sendChatMessage(userId, messageContent);
    }


    public void closeServer() {
        snakeServer.closeServer();
    }
}