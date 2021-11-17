package client.test;

import com.noscompany.snake.game.client.ClientError;
import com.noscompany.snake.game.client.ClientEventHandler;
import com.noscompany.snake.game.client.SnakeOnlineClientCreator;
import com.noscompany.snake.game.client.StartingClientError;
import com.noscompany.snake.game.commons.messages.events.lobby.*;
import snake.game.core.events.*;

class Main {

    public static void main(String[] args) {
        SnakeOnlineClientCreator
                .createClient(new ConsolePrinter())
                .connect("127.0.0.1", "8080");
    }

    private static class ConsolePrinter implements ClientEventHandler {

        @Override
        public void connectionEstablished() {
            System.out.println("connection established");
        }

        @Override
        public void handle(ClientError clientError) {
            System.out.println(clientError);
        }

        @Override
        public void handle(StartingClientError startingClientError) {
            System.out.println(startingClientError);
        }

        @Override
        public void connectionClosed() {
            System.out.println("connection closed");
        }

        @Override
        public void handle(NewUserAdded event) {
            System.out.println(event);
        }

        @Override
        public void handle(NewUserConnectedAsAdmin event) {
            System.out.println(event);
        }

        @Override
        public void handle(UserRemoved event) {
            System.out.println(event);
        }

        @Override
        public void handle(GameOptionsChanged event) {
            System.out.println(event);
        }

        @Override
        public void handle(PlayerTookASeat event) {
            System.out.println(event);
        }

        @Override
        public void handle(PlayerFreedUpASeat event) {
            System.out.println(event);
        }

        @Override
        public void handle(FailedToStartGame event) {
            System.out.println(event);
        }

        @Override
        public void handle(FailedToTakeASeat event) {
            System.out.println(event);
        }

        @Override
        public void handle(FailedToChangeGameOptions event) {
            System.out.println(event);
        }

        @Override
        public void handle(ChatMessageReceived event) {
            System.out.println(event);
        }

        @Override
        public void handle(TimeLeftToGameStartHasChanged event) {
            System.out.println(event);
        }

        @Override
        public void handle(GameStarted event) {
            System.out.println(event);
        }

        @Override
        public void handle(GameContinues event) {
            System.out.println(event);
        }

        @Override
        public void handle(GameFinished event) {
            System.out.println(event);
        }

        @Override
        public void handle(GameCancelled event) {
            System.out.println(event);
        }

        @Override
        public void handle(GamePaused event) {
            System.out.println(event);
        }

        @Override
        public void handle(GameResumed event) {
            System.out.println(event);
        }
    }
}