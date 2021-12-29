package com.noscompany.snake.game.online.server.room.message.handler;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToChangeGameOptions;
import com.noscompany.snake.game.commons.messages.events.lobby.GameOptionsChanged;
import com.noscompany.snake.game.online.server.room.message.handler.commons.BaseTestClass;
import org.junit.Test;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Walls;

public class ChangingGameOptionsTest extends BaseTestClass {

    @Test
    public void gameOptionsChangedEventShouldBeSentToTheBroadcaster() {
//        given that the user entered the room
        enterTheRoom(userId, randomValidUserName());
//        and he took a seat
        takeASeat(userId, anyFreeSeatNumber());
//        when he tries to change game options
        changeGameOptions(userId, gridSize(), gameSpeed(), walls());
//        then "GameOptionsChanged" event should be sent via broadcaster
        var expectedMessage = new GameOptionsChanged(lobbyState());
        assert getBroadcasterMessages().contains(expectedMessage);
//        and no message is sent through any resource directly
        assert getAllResourceMessages().isEmpty();
    }

    @Test
    public void failedToChangeGameOptionsEventShouldBeSentToTheUserResourceOnly() {
//        when the user tries to change game options
        changeGameOptions(userId, gridSize(), gameSpeed(), walls());
//        then user's resource receives "FailedToChangeGameOptions" event
        var expectedMessage = FailedToChangeGameOptions.userNotInTheRoom();
        assert getResourceMessagesById(userId).contains(expectedMessage);
//        and no message is sent via broadcaster
        assert getBroadcasterMessages().isEmpty();
    }

    private Walls walls() {
        return Walls.ON;
    }

    private GameSpeed gameSpeed() {
        return GameSpeed.x1;
    }

    private GridSize gridSize() {
        return GridSize._10x10;
    }
}