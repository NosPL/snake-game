package com.noscompany.snake.online.user.registry;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@AllArgsConstructor
public class UserRegistry {
    private final Map<UserId, UserName> userNamesById;
    private final int playerLimit;
    @Getter
    private final int maxUsernameLength;

    public boolean isFull() {
        return userNamesById.size() >= playerLimit;
    }

    public Either<FailedToEnterRoom, NewUserEnteredRoom> enterRoom(UserId userId, UserName userName) {
        log.info("user with id: {} and name: {} tries to enter the room", userId.getId(), userName.getName());
        return enter(userId, userName)
                .peekLeft(failure -> log.info("user failed to enter the room, reason: {}", failure.getReason()))
                .peek(success -> log.info("user entered the room"));
    }

    private Either<FailedToEnterRoom, NewUserEnteredRoom> enter(UserId userId, UserName userName) {
        if (this.isFull())
            return Either.left(FailedToEnterRoom.roomIsFull(userId));
        UserName currentUserName = userNamesById.get(userId);
        if (currentUserName != null)
            return Either.left(FailedToEnterRoom.userAlreadyInTheRoom(userId));
        if (getUserNames().contains(userName))
            return Either.left(FailedToEnterRoom.userNameAlreadyInUse(userId));
        if (!userNameIsValid(userName.getName()))
            return Either.left(FailedToEnterRoom.incorrectUserNameFormat(userId));
        userNamesById.put(userId, userName);
        return Either.right(new NewUserEnteredRoom(userId, userName, getUserNames()));
    }

    private boolean userNameIsValid(String userName) {
        return !userName.isBlank() && userName.codePoints().count() <= maxUsernameLength;
    }

    public Option<UserLeftRoom> leaveTheRoom(UserId userId) {
        log.info("user with id: {} tries to leave the room", userId.getId());
        return Option
                .of(userNamesById.remove(userId))
                .map(userName -> new UserLeftRoom(userId, userName, getUserNames()))
                .peek(event -> log.info("user left the room"));
    }

    public Set<UserName> getUserNames() {
        return new HashSet<>(userNamesById.values());
    }

    public boolean hasUserWithId(UserId userId) {
        return userNamesById.containsKey(userId);
    }

    public void removeAllUsers() {
        userNamesById.clear();
    }
}