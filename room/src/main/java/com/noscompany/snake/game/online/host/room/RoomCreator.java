package com.noscompany.snake.game.online.host.room;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.ports.GameplayEventHandler;

public interface RoomCreator {
    Room createRoom(GameplayCreator gameplayCreator,
                    UsersCountLimit usersCountLimit,
                    MessagePublisher messagePublisher);

}