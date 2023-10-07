package com.noscompany.snake.game.test.client;

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
import com.noscompany.snake.game.online.contract.messages.server.*;
import com.noscompany.snake.game.online.host.RoomEventHandlerForHost;

class NullHostEventHandler implements RoomEventHandlerForHost {

    @Override
    public void handle(FailedToStartServer serverFailedToStart) {

    }

    @Override
    public void handle(ServerFailedToSendMessageToRemoteClients event) {

    }

    @Override
    public void handle(ServerStarted serverStarted) {

    }

    @Override
    public void handle(FailedToEnterRoom failedToEnterRoom) {

    }

    @Override
    public void handle(UserSentChatMessage userSentChatMessage) {

    }

    @Override
    public void handle(GameOptionsChanged gameOptionsChanged) {

    }

    @Override
    public void handle(FailedToSendChatMessage event) {

    }

    @Override
    public void handle(FailedToStartGame event) {

    }

    @Override
    public void handle(FailedToChangeGameOptions event) {

    }

    @Override
    public void handle(PlayerFreedUpASeat event) {

    }

    @Override
    public void handle(FailedToFreeUpSeat event) {

    }

    @Override
    public void handle(PlayerTookASeat event) {

    }

    @Override
    public void handle(FailedToTakeASeat event) {

    }

    @Override
    public void handle(GameStartCountdown event) {

    }

    @Override
    public void handle(GameStarted event) {

    }

    @Override
    public void handle(SnakesMoved event) {

    }

    @Override
    public void handle(GameFinished event) {

    }

    @Override
    public void handle(GameCancelled event) {

    }

    @Override
    public void handle(GamePaused event) {

    }

    @Override
    public void handle(GameResumed event) {

    }

    @Override
    public void handle(NewUserEnteredRoom event) {

    }

    @Override
    public void handle(UserLeftRoom event) {

    }

    @Override
    public void handle(ServerGotShutdown event) {

    }

    @Override
    public void hostEnteredRoom() {

    }
}