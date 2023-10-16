package com.noscompany.snake.game.test.client;

import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.snake.game.online.chat.ChatConfiguration;
import com.noscompany.snake.game.online.client.*;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameSpeed;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.playground.GameReinitialized;
import com.noscompany.snake.game.online.contract.messages.playground.InitializeGame;
import com.noscompany.snake.game.online.contract.messages.seats.*;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UsersCountLimit;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageDeserializer;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageSerializer;
import com.noscompany.snake.game.online.playground.PlaygroundConfiguration;
import com.noscompany.snake.game.online.websocket.WebsocketConfiguration;
import com.noscompany.snake.online.user.registry.UserRegistryConfiguration;
import snake.game.gameplay.GameplayConfiguration;

public class SnakeOnlineTestClientConfiguration {

    public SnakeOnlineClient snakeOnlineTestClient() {
        var snakeOnlineClient = new SnakeOnlineClientConfiguration().create(new MessagePublisherCreator().create());
        var serializer = OnlineMessageSerializer.instance();
        var deserializer = OnlineMessageDeserializer.instance();
        var hostMessagePublisher = new MessagePublisherCreator().create();
        var websocketCreator = new WebsocketConfiguration().websocketCreator();
        var server = new ServerConfiguration().server(websocketCreator, hostMessagePublisher, serializer, deserializer);
        new UserRegistryConfiguration().create(new UsersCountLimit(10), hostMessagePublisher);
        new ChatConfiguration().createChat(hostMessagePublisher);
        var gameplayCreator = new GameplayConfiguration().snakeGameplayCreator();
        new PlaygroundConfiguration().createPlayground(hostMessagePublisher, gameplayCreator, new GameOptions(GridSize._10x10, GameSpeed.x1, Walls.ON));
        return new SnakeOnlineTestClient(snakeOnlineClient, new Null(), server);
    }

    private class Null implements ClientEventHandler {

        @Override
        public void connectionEstablished(ConnectionEstablished event) {

        }

        @Override
        public void sendClientMessage(SendClientMessageError sendClientMessageError) {

        }

        @Override
        public void startingClientError(StartingClientError startingClientError) {

        }

        @Override
        public void connectionClosed(ConnectionClosed event) {

        }

        @Override
        public void newUserEnteredRoom(NewUserEnteredRoom event) {

        }

        @Override
        public void failedToEnterRoom(FailedToEnterRoom event) {

        }

        @Override
        public void userLeftRoom(UserLeftRoom event) {

        }

        @Override
        public void initializeSeats(InitializeSeats event) {

        }

        @Override
        public void playerTookASeat(PlayerTookASeat event) {

        }

        @Override
        public void failedToTakeASeat(FailedToTakeASeat event) {

        }

        @Override
        public void playerFreedUpASeat(PlayerFreedUpASeat event) {

        }

        @Override
        public void failedToFreeUpASeat(FailedToFreeUpSeat event) {

        }

        @Override
        public void initializePlayground(InitializeGame event) {

        }

        @Override
        public void gameReinitialized(GameReinitialized event) {

        }

        @Override
        public void gameOptionsChanged(GameOptionsChanged event) {

        }

        @Override
        public void failedToChangeGameOptions(FailedToChangeGameOptions event) {

        }

        @Override
        public void userSentChatMessage(UserSentChatMessage event) {

        }

        @Override
        public void failedToSendChatMessage(FailedToSendChatMessage event) {

        }

        @Override
        public void failedToStartGame(FailedToStartGame event) {

        }

        @Override
        public void gameStartCountdown(GameStartCountdown event) {

        }

        @Override
        public void gameStarted(GameStarted event) {

        }

        @Override
        public void snakesMoved(SnakesMoved event) {

        }

        @Override
        public void gameFinished(GameFinished event) {

        }

        @Override
        public void gameCancelled(GameCancelled event) {

        }

        @Override
        public void failedToCancelGame(FailedToCancelGame event) {

        }

        @Override
        public void gamePaused(GamePaused event) {

        }

        @Override
        public void failedToPauseGame(FailedToPauseGame event) {

        }

        @Override
        public void gameResumed(GameResumed event) {

        }

        @Override
        public void failedToResumeGame(FailedToResumeGame event) {

        }

        @Override
        public void serverGotShutdown(ServerGotShutdown event) {

        }
    }
}