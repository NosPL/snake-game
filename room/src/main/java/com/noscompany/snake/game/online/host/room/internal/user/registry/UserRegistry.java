package com.noscompany.snake.game.online.host.room.internal.user.registry;

import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public class UserRegistry {
    private final Map<String, String> userNamesById;
    private final int playerLimit;

    public synchronized boolean isFull() {
        return userNamesById.size() >= playerLimit;
    }

    public synchronized Option<FailedToEnterRoom> registerNewUser(String userId, String userName) {
        if (this.isFull())
            return Option.of(FailedToEnterRoom.roomIsFull(userName));
        String currentUserName = userNamesById.get(userId);
        if (currentUserName != null)
            return Option.of(FailedToEnterRoom.userAlreadyInTheRoom(currentUserName));
        if (getUserNames().contains(userName))
            return Option.of(FailedToEnterRoom.userNameAlreadyInUse(userName));
        if (!userNameIsValid(userName))
            return Option.of(FailedToEnterRoom.incorrectUserNameFormat(userName));
        userNamesById.put(userId, userName);
        return Option.none();
    }

    boolean userNameIsValid(String userName) {
        return !userName.isBlank() && userName.codePoints().count() <= 15;
    }

    public Option<UserRemoved> removeUser(String userId) {
        return Option
                .of(userNamesById.remove(userId))
                .map(userName -> new UserRemoved(userId, userName));
    }

    public Option<String> findUserNameById(String userId) {
        return Option.of(userNamesById.get(userId
        ));
    }

    public synchronized Set<String> getUserNames() {
        return new HashSet<>(userNamesById.values());
    }

    public boolean containsId(String userId) {
        return userNamesById.containsKey(userId);
    }

    @Value
    public
    class UserRemoved {
        String userId;
        String userName;
    }
}