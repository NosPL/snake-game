package com.noscompany.snake.game.online.server.room.message.handler;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToStartGame;
import com.noscompany.snake.game.online.server.room.message.handler.commons.BaseTestClass;
import org.junit.Test;

public class StartingGameTest extends BaseTestClass {

    @Test
    public void failedToStartGameShouldBeSentToTheUserResourceOnly() {
//        when the user tries to start game
        startGame(userId);
//        then "FailedToStartGame" event is sent to the user's resource
        var expectedMessage = FailedToStartGame.userIsNotInTheRoom(lobbyState());
        assert getResourceMessagesById(userId).contains(expectedMessage);
//        and no message is sent via broadcaster
        assert getBroadcasterMessages().isEmpty();
    }
}