package com.noscompany.snake.game.commons.messages.events.lobby;

public interface LobbyEventHandler {

    void handle(NewUserAdded event);

    void handle(NewUserConnectedAsAdmin event);

    void handle(UserRemoved event);

    void handle(GameOptionsChanged event);

    void handle(PlayerTookASeat event);

    void handle(PlayerFreedUpASeat event);

    void handle(FailedToStartGame event);

    void handle(FailedToTakeASeat event);

    void handle(FailedToChangeGameOptions event);

    void handle(ChatMessageReceived event);
}