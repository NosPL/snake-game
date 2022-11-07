package com.noscompany.snake.game.online.host.server.javalin.internal.state.running;

import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.host.room.message.dispatcher.RoomCommandHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
 abstract class DeserializedMessage {
    protected final RemoteClientId remoteClientId;

     abstract void applyTo(RoomCommandHandlerForRemoteClients handler);

     static class EnterRoom extends DeserializedMessage {
        private final String userName;

         EnterRoom(RemoteClientId remoteClientId, String userName) {
            super(remoteClientId);
            this.userName = userName;
        }

        @Override
         void applyTo(RoomCommandHandlerForRemoteClients roomCommandHandler) {
            roomCommandHandler.enterRoom(remoteClientId, userName);
        }
    }

     static class TakeASeat extends DeserializedMessage {
        private final PlayerNumber playerNumber;

         TakeASeat(RemoteClientId remoteClientId, PlayerNumber playerNumber) {
            super(remoteClientId);
            this.playerNumber = playerNumber;
        }

        @Override
         void applyTo(RoomCommandHandlerForRemoteClients roomCommandHandler) {
            roomCommandHandler.takeASeat(remoteClientId, playerNumber);
        }
    }

     static class FreeUpASeat extends DeserializedMessage {

         FreeUpASeat(RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
         void applyTo(RoomCommandHandlerForRemoteClients roomCommandHandler) {
            roomCommandHandler.freeUpSeat(remoteClientId);
        }
    }

     static class ChangeGameOptions extends DeserializedMessage {
        private final GameOptions gameOptions;

         ChangeGameOptions(RemoteClientId remoteClientId, GameOptions gameOptions) {
            super(remoteClientId);
            this.gameOptions = gameOptions;
        }

        @Override
         void applyTo(RoomCommandHandlerForRemoteClients roomCommandHandler) {
            roomCommandHandler.changeGameOptions(remoteClientId, gameOptions);
        }
    }

     static class ChangeSnakeDirection extends DeserializedMessage {
        private final Direction direction;

         ChangeSnakeDirection(RemoteClientId remoteClientId, Direction direction) {
            super(remoteClientId);
            this.direction = direction;
        }

        @Override
         void applyTo(RoomCommandHandlerForRemoteClients roomCommandHandler) {
            roomCommandHandler.changeDirection(remoteClientId, direction);
        }
    }

     static class StartGame extends DeserializedMessage {

         StartGame(RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
         void applyTo(RoomCommandHandlerForRemoteClients roomCommandHandler) {
            roomCommandHandler.startGame(remoteClientId);
        }
    }

     static class CancelGame extends DeserializedMessage {

         CancelGame(RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
         void applyTo(RoomCommandHandlerForRemoteClients roomCommandHandler) {
            roomCommandHandler.cancelGame(remoteClientId);
        }
    }

     static class PauseGame extends DeserializedMessage {

         PauseGame(RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
         void applyTo(RoomCommandHandlerForRemoteClients roomCommandHandler) {
            roomCommandHandler.pauseGame(remoteClientId);
        }
    }

     static class ResumeGame extends DeserializedMessage {

         ResumeGame(RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
         void applyTo(RoomCommandHandlerForRemoteClients roomCommandHandler) {
            roomCommandHandler.resumeGame(remoteClientId);
        }
    }

     static class SendChatMessage extends DeserializedMessage {
        private final String messageContent;

         SendChatMessage(RemoteClientId remoteClientId, String messageContent) {
            super(remoteClientId);
            this.messageContent = messageContent;
        }

        @Override
         void applyTo(RoomCommandHandlerForRemoteClients roomCommandHandler) {
            roomCommandHandler.sendChatMessage(remoteClientId, messageContent);
        }
    }
}