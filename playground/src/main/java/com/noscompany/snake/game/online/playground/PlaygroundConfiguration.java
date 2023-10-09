package com.noscompany.snake.game.online.playground;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameSpeed;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;
import com.noscompany.snake.game.online.contract.messages.host.HostGotShutdown;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.server.events.NewRemoteClientConnected;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import snake.game.gameplay.GameplayCreator;

import java.util.HashMap;
import java.util.Set;

public final class PlaygroundConfiguration {

    public Playground createPlayground(MessagePublisher messagePublisher, GameplayCreator gameplayCreator) {
        var gameplayEventHandler = new GamePlayEventsMessagePublisherAdapter(messagePublisher);
        var gameCreatorAdapter = new GameCreatorAdapter(gameplayCreator, gameplayEventHandler);
        var gameOptions = new GameOptions(GridSize._10x10, GameSpeed.x1, Walls.ON);
        var snakeGame = gameCreatorAdapter.createGame(Set.of(), gameOptions);
        var playground = new Playground(new HashMap<>(), SeatsCreator.create(), gameCreatorAdapter, gameOptions, snakeGame);
        var subscription = createSubscription(playground);
        messagePublisher.subscribe(subscription);
        return playground;
    }

    private Subscription createSubscription(Playground playground) {
        return new Subscription()
//                user registry events
                .toMessage(NewUserEnteredRoom.class, (NewUserEnteredRoom msg) -> playground.newUserEnteredRoom(msg.getUserId(), new UserName(msg.getUserName())))
                .toMessage(UserLeftRoom.class, (UserLeftRoom msg) -> playground.userLeftRoom(msg.getUserId()))
//                seats commands
                .toMessage(FreeUpASeat.class, (FreeUpASeat msg) -> playground.freeUpASeat(msg.getUserId()))
                .toMessage(TakeASeat.class, (TakeASeat msg) -> playground.takeASeat(msg.getUserId(), msg.getPlayerNumber()))
//                game options
                .toMessage(ChangeGameOptions.class, (ChangeGameOptions msg) -> playground.changeGameOptions(msg.getUserId(), msg.getOptions()))
                .toMessage(TakeASeat.class, (TakeASeat msg) -> playground.takeASeat(msg.getUserId(), msg.getPlayerNumber()))
//                gameplay commands
                .toMessage(StartGame.class, (StartGame msg) -> playground.startGame(msg.getUserId()))
                .toMessage(ChangeSnakeDirection.class, (ChangeSnakeDirection msg) -> playground.changeSnakeDirection(msg.getUserId(), msg.getDirection()))
                .toMessage(CancelGame.class, (CancelGame msg) -> playground.cancelGame(msg.getUserId()))
                .toMessage(PauseGame.class, (PauseGame msg) -> playground.pauseGame(msg.getUserId()))
                .toMessage(ResumeGame.class, (ResumeGame msg) -> playground.resumeGame(msg.getUserId()))
//                host events
                .toMessage(HostGotShutdown.class, (HostGotShutdown msg) -> playground.terminate())
                .subscriberName("playground");
    }
}