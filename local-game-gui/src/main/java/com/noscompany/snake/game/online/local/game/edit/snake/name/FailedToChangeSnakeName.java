package com.noscompany.snake.game.online.local.game.edit.snake.name;

enum FailedToChangeSnakeName {
    NAME_CANNOT_BE_LONGER_THAN_15_SIGNS,
    NAME_CANNOT_BE_EMPTY;

    String astTest() {
        return toString()
                .toLowerCase()
                .replace("_", " ");
    }
}