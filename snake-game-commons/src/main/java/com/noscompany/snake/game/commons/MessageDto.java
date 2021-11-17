package com.noscompany.snake.game.commons;

public interface MessageDto {
    MessageType getMessageType();

    enum MessageType {
        //lobby commands
        TAKE_A_SEAT,
        FREE_UP_A_SEAT,
        CHANGE_GAME_OPTIONS,

        //chat commands
        SEND_CHAT_MESSAGE,

        //game commands,
        START_GAME,
        CHANGE_SNAKE_DIRECTION,
        CANCEL_GAME,
        PAUSE_GAME,
        RESUME_GAME,

        //client-server events
        NEW_USER_ADDED,
        NEW_USER_ADDED_AS_ADMIN,
        USER_REMOVED,
        FAILED_TO_ADD_USER,

        //lobby events,
        GAME_OPTIONS_CHANGED,
        FAILED_TO_CHANGE_GAME_OPTIONS,
        PLAYER_TOOK_A_SEAT,
        FAILED_TO_TAKE_A_SEAT,
        PLAYER_FREED_UP_SEAT,
        FAILED_TO_START_GAME,

        //chat events
        CHAT_MESSAGE_RECEIVED,

        //game events
        TIME_LEFT_TO_GAME_START_CHANGED,
        GAME_STARTED,
        GAME_CONTINUES,
        GAME_FINISHED,
        GAME_CANCELLED,
        GAME_PAUSED,
        GAME_RESUMED
    }
}