package com.noscompany.snake.online.user.registry.test;

import com.noscompany.message.publisher.utils.NullMessagePublisher;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.contract.messages.user.registry.UsersCountLimit;
import com.noscompany.snake.online.user.registry.UserRegistry;
import com.noscompany.snake.online.user.registry.UserRegistryConfiguration;
import org.junit.Before;

import java.util.List;
import java.util.UUID;

public class Setup {
    protected UserId actorId;
    protected UserRegistry userRegistry;
    protected UserName actorName;

    @Before
    public void init() {
        userRegistry = new UserRegistryConfiguration().create(new UsersCountLimit(10), new NullMessagePublisher());
        actorId = UserId.random();
        actorName = correctUniqueUsername();
    }


    protected UserName correctUniqueUsername() {
        String userNameStr = UUID.randomUUID().toString();
        if (userNameStr.codePoints().count() > getMaxUsernameLength())
            return new UserName(userNameStr.substring(0, getMaxUsernameLength()));
        else
            return new UserName(userNameStr);
    }

    protected UserName empty() {
        return new UserName("");
    }

    protected UserName onlySpaces() {
        return new UserName("   ");
    }

    protected UserName onlyTabs() {
        return new UserName("       ");
    }

    protected UserName lineSeparator() {
        return new UserName(System.lineSeparator());
    }

    protected List<UserName> blankNames() {
        return List.of(onlySpaces(), onlyTabs(), empty(), lineSeparator(), tooLongUsername());
    }

    protected UserName tooLongUsername() {
        UserName userName = new UserName(UUID.randomUUID().toString());
        assert userName.getName().codePoints().count() > userRegistry.getMaxUsernameLength();
        return userName;
    }

    private int getMaxUsernameLength() {
        return userRegistry.getMaxUsernameLength();
    }
}