package com.noscompany.snake.game.online.host.server.internal.state.running;

import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.room.PlayerName;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
 abstract class DeserializedMessage {
    protected final RoomApiForRemoteClients.RemoteClientId remoteClientId;

     abstract void applyTo(RoomApiForRemoteClients handler);

     static class EnterRoom extends DeserializedMessage {
        private final String userName;

         EnterRoom(RoomApiForRemoteClients.RemoteClientId remoteClientId, String userName) {
            super(remoteClientId);
            this.userName = userName;
        }

        @Override
         void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.enterRoom(remoteClientId, new PlayerName(userName));
        }
    }

     static class TakeASeat extends DeserializedMessage {
        private final PlayerNumber playerNumber;

         TakeASeat(RoomApiForRemoteClients.RemoteClientId remoteClientId, PlayerNumber playerNumber) {
            super(remoteClientId);
            this.playerNumber = playerNumber;
        }

        @Override
         void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.takeASeat(remoteClientId, playerNumber);
        }
    }

     static class FreeUpASeat extends DeserializedMessage {

         FreeUpASeat(RoomApiForRemoteClients.RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
         void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.freeUpSeat(remoteClientId);
        }
    }

     static class ChangeGameOptions extends DeserializedMessage {
        private final GameOptions gameOptions;

         ChangeGameOptions(RoomApiForRemoteClients.RemoteClientId remoteClientId, GameOptions gameOptions) {
            super(remoteClientId);
            this.gameOptions = gameOptions;
        }

        @Override
         void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.changeGameOptions(remoteClientId, gameOptions);
        }
    }

     static class ChangeSnakeDirection extends DeserializedMessage {
        private final Direction direction;

         ChangeSnakeDirection(RoomApiForRemoteClients.RemoteClientId remoteClientId, Direction direction) {
            super(remoteClientId);
            this.direction = direction;
        }

        @Override
         void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.changeDirection(remoteClientId, direction);
        }
    }

     static class StartGame extends DeserializedMessage {

         StartGame(RoomApiForRemoteClients.RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
         void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.startGame(remoteClientId);
        }
    }

     static class CancelGame extends DeserializedMessage {

         CancelGame(RoomApiForRemoteClients.RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
         void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.cancelGame(remoteClientId);
        }
    }

     static class PauseGame extends DeserializedMessage {

         PauseGame(RoomApiForRemoteClients.RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
         void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.pauseGame(remoteClientId);
        }
    }

     static class ResumeGame extends DeserializedMessage {

         ResumeGame(RoomApiForRemoteClients.RemoteClientId remoteClientId) {
            super(remoteClientId);
        }

        @Override
         void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.resumeGame(remoteClientId);
        }
    }

     static class SendChatMessage extends DeserializedMessage {
        private final String messageContent;

         SendChatMessage(RoomApiForRemoteClients.RemoteClientId remoteClientId, String messageContent) {
            super(remoteClientId);
            this.messageContent = messageContent;
        }

        @Override
         void applyTo(RoomApiForRemoteClients roomCommandHandler) {
            roomCommandHandler.sendChatMessage(remoteClientId, messageContent);
        }
    }
}