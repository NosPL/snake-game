package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.host.HostGotShutdown;
import com.noscompany.snake.game.online.contract.messages.room.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.server.events.RemoteClientDisconnected;
import com.noscompany.snake.game.online.host.room.RoomCreator;
import snake.game.gameplay.GameplayCreator;


public class MediatorConfiguration {

    public void configureMediator(MessagePublisher messagePublisher,
                                  RoomCreator roomCreator,
                                  UsersCountLimit usersCountLimit,
                                  GameplayCreator gameplayCreator) {
        var gameplayEventHandler = new GameplayEventHandlerMessagePublisherAdapter(messagePublisher);
        var room = roomCreator.createRoom(gameplayEventHandler, gameplayCreator, usersCountLimit);
        var commandHandler = new Mediator(room, messagePublisher);
        var subscription = createSubscription(commandHandler);
        messagePublisher.subscribe(subscription);
    }

    Subscription createSubscription(Mediator commandQueue) {
        return new Subscription()
                .subscriberName("mediator")
//                user registry commands
                .toMessage(EnterRoom.class, commandQueue::enterRoom)
//                chat commands
                .toMessage(SendChatMessage.class, commandQueue::sendChatMessage)
//                game options
                .toMessage(ChangeGameOptions.class, commandQueue::changeGameOptions)
//                seats commands
                .toMessage(FreeUpASeat.class, commandQueue::freeUpASeat)
                .toMessage(TakeASeat.class, commandQueue::takeASeat)
//                gameplay commands
                .toMessage(StartGame.class, commandQueue::startGame)
                .toMessage(ChangeSnakeDirection.class, commandQueue::changeSnakeDirection)
                .toMessage(CancelGame.class, commandQueue::cancelGame)
                .toMessage(PauseGame.class, commandQueue::pauseGame)
                .toMessage(ResumeGame.class, commandQueue::resumeGame)
//                seats commands
                .toMessage(FreeUpASeat.class, commandQueue::freeUpASeat)
                .toMessage(TakeASeat.class, commandQueue::takeASeat)
//                host events
                .toMessage(HostGotShutdown.class, commandQueue::terminateRoom)
//                server events
                .toMessage(RemoteClientDisconnected.class, commandQueue::remoteClientDisconnected);
    }
}