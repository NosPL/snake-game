package com.noscompany.snake.game.online.host.room;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.host.HostGotShutdown;
import com.noscompany.snake.game.online.contract.messages.room.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.server.events.RemoteClientDisconnected;
import com.noscompany.snake.game.online.host.room.internal.chat.ChatCreator;
import com.noscompany.snake.game.online.host.room.internal.playground.PlaygroundCreator;
import com.noscompany.snake.game.online.host.room.internal.user.registry.UserRegistryCreator;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.ports.GameplayEventHandler;

class RoomCreatorImpl implements RoomCreator{

    public Room createRoom(GameplayCreator gameplayCreator,
                           UsersCountLimit usersCountLimit,
                           MessagePublisher messagePublisher) {
        var room = new LogsDecorator(new RoomFacade(
                UserRegistryCreator.create(usersCountLimit),
                PlaygroundCreator.create(messagePublisher, gameplayCreator),
                ChatCreator.create()));
        messagePublisher.subscribe(createSubscription(room));
        return room;
    }

    Subscription createSubscription(Room room) {
        return new Subscription()
                .subscriberName("mediator")
//                user registry commands
                .toMessage(EnterRoom.class, (EnterRoom msg) -> room.enter(msg.getUserId(), new UserName(msg.getUserName())))
//                chat commands
                .toMessage(SendChatMessage.class, (SendChatMessage msg) -> room.sendChatMessage(msg.getUserId(), msg.getMessageContent()))
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
                .toMessage(HostGotShutdown.class, (HostGotShutdown msg) -> room.terminate())
//                server events
                .toMessage(RemoteClientDisconnected.class, (RemoteClientDisconnected msg) -> room.leave(msg.getRemoteClientId()));
    }
}