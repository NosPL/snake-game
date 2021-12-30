package com.noscompany.snake.game.commons;

public interface OnlineMessage {
    MessageType getMessageType();

    enum MessageType {
        //room commands
        ENTER_THE_ROOM,

        //room event
        FAILED_TO_ENTER_THE_ROOM,
        NEW_USER_ENTERED_ROOM,
        USER_LEFT_ROOM,

        //lobby commands
        START_GAME,
        TAKE_A_SEAT,
        FREE_UP_A_SEAT,
        CHANGE_GAME_OPTIONS,

        //lobby events
        GAME_OPTIONS_CHANGED,
        FAILED_TO_CHANGE_GAME_OPTIONS,
        PLAYER_TOOK_A_SEAT,
        FAILED_TO_TAKE_A_SEAT,
        PLAYER_FREED_UP_SEAT,
        FAILED_TO_FREE_UP_A_SEAT,
        FAILED_TO_START_GAME,

        //game commands
        CHANGE_SNAKE_DIRECTION,
        CANCEL_GAME,
        PAUSE_GAME,
        RESUME_GAME,

        //game events
        TIME_LEFT_TO_GAME_START_CHANGED,
        GAME_STARTED,
        GAME_CONTINUES,
        GAME_FINISHED,
        GAME_CANCELLED,
        GAME_PAUSED,
        GAME_RESUMED,

        //chat commands
        SEND_CHAT_MESSAGE,

        //chat events,
        USER_SENT_CHAT_MESSAGE,
        FAILED_TO_SEND_CHAT_MESSAGE
        }
}