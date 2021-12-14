package com.noscompany.snakejavafxclient.game.scoreboard.controller.scoreboard;

public class ScoreboardCreator {
    private static final String PLACE_HEADER = "PLACE  ";
    private static final String SCORE_HEADER = "SCORE  ";
    private static final String SNAKES_HEADER = "SNAKES";
    private static final int ROW_SIZE = 6;

    public static Scoreboard create() {
        var scoreBoard = new ScoreboardImpl();
        scoreBoard.setHeaders(PLACE_HEADER, SCORE_HEADER, SNAKES_HEADER);
        for (int rowIndex = 1; rowIndex < 5; rowIndex++) {
            scoreBoard.addRowOfSize(rowIndex, ROW_SIZE);
        }
        return scoreBoard;
    }
}