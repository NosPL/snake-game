package com.noscompany.snake.game.online.contract.messages;

public interface OnlineMessage {
    MessageType getMessageType();

    enum MessageType {
        //server event
        SERVER_GOT_SHUTDOWN,

        //user registry
        ENTER_THE_ROOM,
        FAILED_TO_ENTER_THE_ROOM,
        NEW_USER_ENTERED_ROOM,
        USER_LEFT_ROOM,

        //playground
        CHANGE_GAME_OPTIONS,
        INITIALIZE_PLAYGROUND_STATE,
        GAME_OPTIONS_CHANGED,
        FAILED_TO_CHANGE_GAME_OPTIONS,
        GAME_REINITIALIZED,

        //seats commands
        INITIALIZE_SEATS_TO_REMOTE_CLIENT,
        TAKE_A_SEAT,
        FREE_UP_A_SEAT,
        PLAYER_TOOK_A_SEAT,
        FAILED_TO_TAKE_A_SEAT,
        PLAYER_FREED_UP_SEAT,
        FAILED_TO_FREE_UP_A_SEAT,

        //gameplay commands,
        START_GAME,
        CHANGE_SNAKE_DIRECTION,
        CANCEL_GAME,
        PAUSE_GAME,
        RESUME_GAME,

        TIME_LEFT_TO_GAME_START_CHANGED,
        GAME_STARTED,
        SNAKES_MOVED,
        GAME_FINISHED,
        GAME_CANCELLED,
        GAME_PAUSED,
        GAME_RESUMED,

        FAILED_TO_START_GAME,
        FAILED_TO_CANCEL_GAME,
        FAILED_TO_CHANGE_DIRECTION,
        FAILED_TO_RESUME_GAME,
        FAILED_TO_PAUSE_GAME,

        //chat
        SEND_CHAT_MESSAGE,
        USER_SENT_CHAT_MESSAGE,
        FAILED_TO_SEND_CHAT_MESSAGE,

        YOUR_ID_GOT_INITIALIZED,
        INITIALIZE_GAME_OPTIONS
    }
}