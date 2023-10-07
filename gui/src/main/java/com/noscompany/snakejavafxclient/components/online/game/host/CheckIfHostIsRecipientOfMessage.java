package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.server.ServerFailedToSendMessageToRemoteClients;
import com.noscompany.snake.game.online.contract.messages.server.FailedToStartServer;
import com.noscompany.snake.game.online.contract.messages.server.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.server.ServerStarted;
import com.noscompany.snake.game.online.host.RoomEventHandlerForHost;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class CheckIfHostIsRecipientOfMessage implements RoomEventHandlerForHost {
    private final UserId hostId;
    private final RoomEventHandlerForHost hostEventHandler;

    @Override
    public void handle(FailedToStartServer event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(ServerFailedToSendMessageToRemoteClients event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(ServerStarted serverStarted) {
        hostEventHandler.handle(serverStarted);
    }

    @Override
    public void handle(FailedToEnterRoom event) {
        if (event.getUserId().equals(hostId))
            hostEventHandler.handle(event);
    }

    @Override
    public void handle(UserSentChatMessage event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GameOptionsChanged event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToSendChatMessage event) {
        if (event.getUserId().equals(hostId))
            hostEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToStartGame event) {
        if (event.getUserId().equals(hostId))
            hostEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToChangeGameOptions event) {
        if (event.getUserId().equals(hostId))
            hostEventHandler.handle(event);
    }

    @Override
    public void handle(PlayerFreedUpASeat event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToFreeUpSeat event) {
        if (event.getUserId().equals(hostId))
            hostEventHandler.handle(event);
    }

    @Override
    public void handle(PlayerTookASeat event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToTakeASeat event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GameStartCountdown event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GameStarted event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(SnakesMoved event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GameFinished event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GameCancelled event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GamePaused event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GameResumed event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(NewUserEnteredRoom event) {
        if (event.getUserId().equals(hostId))
            hostEnteredRoom();
        else
            hostEventHandler.handle(event);
    }

    @Override
    public void handle(UserLeftRoom event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(ServerGotShutdown event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void hostEnteredRoom() {
        hostEventHandler.hostEnteredRoom();
    }
}