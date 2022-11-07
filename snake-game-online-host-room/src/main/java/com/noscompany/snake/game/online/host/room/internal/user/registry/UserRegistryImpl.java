package com.noscompany.snake.game.online.host.room.internal.user.registry;

import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
class UserRegistryImpl implements UserRegistry {
    private final Map<String, String> userNamesById;

    @Override
    public synchronized Option<FailedToEnterRoom> registerNewUser(String userId, String userName) {
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

    @Override
    public boolean userNameIsValid(String userName) {
        return !userName.isBlank() && userName.codePoints().count() <= 15;
    }

    @Override
    public Option<UserRemoved> removeUser(String userId) {
        return Option
                .of(userNamesById.remove(userId))
                .map(userName -> new UserRemoved(userId, userName));
    }

    @Override
    public Option<String> findUserNameById(String userId) {
        return Option.of(userNamesById.get(userId
        ));
    }

    @Override
    public synchronized Set<String> getUserNames() {
        return new HashSet<>(userNamesById.values());
    }

    @Override
    public int getUserCount() {
        return userNamesById.size();
    }

    @Override
    public boolean containsId(String userId) {
        return userNamesById.containsKey(userId);
    }

    @Override
    public int usersCount() {
        return userNamesById.size();
    }
}