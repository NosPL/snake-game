package com.noscompany.snake.game.online.host.room;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.host.HostGotShutdown;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.server.events.NewRemoteClientConnected;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.contract.messages.user.registry.UsersCountLimit;
import com.noscompany.snake.game.online.host.room.internal.playground.PlaygroundCreator;
import snake.game.gameplay.GameplayCreator;

import java.util.HashMap;

class RoomCreatorImpl implements RoomCreator{

    public Room createRoom(GameplayCreator gameplayCreator,
                           UsersCountLimit usersCountLimit,
                           MessagePublisher messagePublisher) {
        var room = new LogsDecorator(new RoomFacade(
                new HashMap<>(),
                PlaygroundCreator.create(messagePublisher, gameplayCreator)));
        messagePublisher.subscribe(createSubscription(room));
        return room;
    }

    Subscription createSubscription(Room room) {
        return new Subscription()
                .subscriberName("room")
//                server events
                .toMessage(NewRemoteClientConnected.class, (NewRemoteClientConnected msg) -> room.newRemoteClientConnected(msg.getRemoteClientId()))
//                user registry events
                .toMessage(NewUserEnteredRoom.class, (NewUserEnteredRoom msg) -> room.newUserEnteredRoom(msg.getUserId(), new UserName(msg.getUserName())))
                .toMessage(UserLeftRoom.class, (UserLeftRoom msg) -> room.userLeftRoom(msg.getUserId()))
//                seats commands
                .toMessage(FreeUpASeat.class, (FreeUpASeat msg) -> room.freeUpASeat(msg.getUserId()))
                .toMessage(TakeASeat.class, (TakeASeat msg) -> room.takeASeat(msg.getUserId(), msg.getPlayerNumber()))
//                game options
                .toMessage(ChangeGameOptions.class, (ChangeGameOptions msg) -> room.changeGameOptions(msg.getUserId(), msg.getOptions()))
                .toMessage(TakeASeat.class, (TakeASeat msg) -> room.takeASeat(msg.getUserId(), msg.getPlayerNumber()))
//                gameplay commands
                .toMessage(StartGame.class, (StartGame msg) -> room.startGame(msg.getUserId()))
                .toMessage(ChangeSnakeDirection.class, (ChangeSnakeDirection msg) -> room.changeSnakeDirection(msg.getUserId(), msg.getDirection()))
                .toMessage(CancelGame.class, (CancelGame msg) -> room.cancelGame(msg.getUserId()))
                .toMessage(PauseGame.class, (PauseGame msg) -> room.pauseGame(msg.getUserId()))
                .toMessage(ResumeGame.class, (ResumeGame msg) -> room.resumeGame(msg.getUserId()))
//                host events
                .toMessage(HostGotShutdown.class, (HostGotShutdown msg) -> room.terminate());
    }
}