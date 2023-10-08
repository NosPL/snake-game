package com.noscompany.snake.game.online.host.room;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.user.registry.UsersCountLimit;
import snake.game.gameplay.GameplayCreator;

public interface RoomCreator {
    Room createRoom(GameplayCreator gameplayCreator,
                    UsersCountLimit usersCountLimit,
                    MessagePublisher messagePublisher);

}