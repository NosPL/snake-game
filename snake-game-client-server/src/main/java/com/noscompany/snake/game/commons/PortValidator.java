package com.noscompany.snake.game.commons;

public class PortValidator {

    public static boolean isValid(String port) {
        try {
            final int portInt = Integer.parseInt(port);
            return portInt >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}