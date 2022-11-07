package com.noscompany.snake.game.online.host.server.message.handler;

import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToStartGame;
import com.noscompany.snake.game.online.host.server.message.handler.commons.BaseTestClass;
import org.junit.Test;

public class StartingGameTest extends BaseTestClass {

    @Test
    public void failedToStartGameShouldBeSentToTheUserResourceOnly() {
//        when the user tries to start game
        startGame(userId);
//        then "FailedToStartGame" event is sent to the user's resource
        var expectedMessage = FailedToStartGame.userIsNotInTheRoom();
        assert getResourceMessagesById(userId).contains(expectedMessage);
//        and no message is sent via broadcaster
        assert getBroadcasterMessages().isEmpty();
    }
}