package server.test;

import com.noscompany.snake.game.commons.messages.events.lobby.*;
import com.noscompany.snake.game.server.local.api.ServerError;
import com.noscompany.snake.game.server.local.api.SnakeServerCreator;
import com.noscompany.snake.game.server.local.api.StartingServerError;
import com.noscompany.snake.game.server.local.api.SnakeServerEventHandler;
import snake.game.core.events.*;

class Main {

    public static void main(String[] args) {
        SnakeServerCreator
                .instance(new SomeHandler())
                .startServer("127.0.0.1", "8080");
    }


    private static class SomeHandler implements SnakeServerEventHandler {

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
        public void serverStarted() {
            System.out.println("server started");
        }

        @Override
        public void handle(StartingServerError error) {
            System.out.println(error);
        }

        @Override
        public void handle(ServerError error) {
            System.out.println(error);
        }

        @Override
        public void serverClosed() {
            System.out.println("server closed");
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