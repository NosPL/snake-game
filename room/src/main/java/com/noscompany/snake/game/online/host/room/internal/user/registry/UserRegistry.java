package com.noscompany.snake.game.online.host.room.internal.user.registry;

import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.host.room.dto.UserId;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public class UserRegistry {
    private final Map<UserId, UserName> userNamesById;
    private final int playerLimit;

    public boolean isFull() {
        return userNamesById.size() >= playerLimit;
    }

    public Option<FailedToEnterRoom> registerNewUser(UserId userId, UserName userName) {
        if (this.isFull())
            return Option.of(FailedToEnterRoom.roomIsFull());
        UserName currentUserName = userNamesById.get(userId);
        if (currentUserName != null)
            return Option.of(FailedToEnterRoom.userAlreadyInTheRoom());
        if (getUserNames().contains(userName))
            return Option.of(FailedToEnterRoom.userNameAlreadyInUse());
        if (!userNameIsValid(userName.getName()))
            return Option.of(FailedToEnterRoom.incorrectUserNameFormat());
        userNamesById.put(userId, userName);
        return Option.none();
    }

    boolean userNameIsValid(String userName) {
        return !userName.isBlank() && userName.codePoints().count() <= 15;
    }

    public Option<UserRemoved> removeUser(UserId userId) {
        return Option
                .of(userNamesById.remove(userId))
                .map(userName -> new UserRemoved(userId, userName));
    }

    public Option<UserName> findUserNameById(UserId userId) {
        return Option.of(userNamesById.get(userId));
    }

    public Set<UserName> getUserNames() {
        return new HashSet<>(userNamesById.values());
    }

    public boolean containsId(UserId userId) {
        return userNamesById.containsKey(userId);
    }

    @Value
    public
    class UserRemoved {
        UserId userId;
        UserName userName;
    }
}