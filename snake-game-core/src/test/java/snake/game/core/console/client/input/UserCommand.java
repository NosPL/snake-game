package snake.game.core.console.client.input;

import lombok.AllArgsConstructor;
import snake.game.core.dto.Direction;

import java.util.function.Consumer;

public interface UserCommand {
    UserCommand onDirectionChange(Consumer<Direction> consumer);

    UserCommand onExit(Runnable runnable);

    UserCommand onPause(Runnable runnable);

    UserCommand onResume(Runnable runnable);

    UserCommand onUnknownCommand(Consumer<String> consumer);

    static UserCommand changeDirection(Direction direction) {
        return new DirectionCommand(direction);
    }

    static UserCommand exitCommand() {
        return new ExitCommand();
    }

    static UserCommand pauseCommand() {
        return new PauseCommand();
    }

    static UserCommand resumeCommand() {
        return new ResumeCommand();
    }

    static UserCommand unknownCommand(String s) {
        return new UnknownCommand(s);
    }
}

@AllArgsConstructor
class DirectionCommand implements UserCommand {
    private final Direction direction;

    @Override
    public UserCommand onDirectionChange(Consumer<Direction> consumer) {
        consumer.accept(direction);
        return this;
    }

    @Override
    public UserCommand onExit(Runnable runnable) {
        return this;
    }

    @Override
    public UserCommand onPause(Runnable runnable) {
        return this;
    }

    @Override
    public UserCommand onResume(Runnable runnable) {
        return this;
    }

    @Override
    public UserCommand onUnknownCommand(Consumer<String> consumer) {
        return this;
    }
}

@AllArgsConstructor
class ExitCommand implements UserCommand {

    @Override
    public UserCommand onDirectionChange(Consumer<Direction> consumer) {
        return this;
    }

    @Override
    public UserCommand onExit(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    public UserCommand onPause(Runnable runnable) {
        return this;
    }

    @Override
    public UserCommand onResume(Runnable runnable) {
        return this;
    }

    @Override
    public UserCommand onUnknownCommand(Consumer<String> consumer) {
        return this;
    }
}

@AllArgsConstructor
class PauseCommand implements UserCommand {

    @Override
    public UserCommand onDirectionChange(Consumer<Direction> consumer) {
        return this;
    }

    @Override
    public UserCommand onExit(Runnable runnable) {
        return this;
    }

    @Override
    public UserCommand onPause(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    public UserCommand onResume(Runnable runnable) {
        return this;
    }

    @Override
    public UserCommand onUnknownCommand(Consumer<String> consumer) {
        return this;
    }
}

@AllArgsConstructor
class ResumeCommand implements UserCommand {

    @Override
    public UserCommand onDirectionChange(Consumer<Direction> consumer) {
        return this;
    }

    @Override
    public UserCommand onExit(Runnable runnable) {
        return this;
    }

    @Override
    public UserCommand onPause(Runnable runnable) {
        return this;
    }

    @Override
    public UserCommand onResume(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    public UserCommand onUnknownCommand(Consumer<String> consumer) {
        return this;
    }
}

@AllArgsConstructor
class UnknownCommand implements UserCommand {
    private final String wrongCommand;

    @Override
    public UserCommand onDirectionChange(Consumer<Direction> consumer) {
        return this;
    }

    @Override
    public UserCommand onExit(Runnable runnable) {
        return this;
    }

    @Override
    public UserCommand onPause(Runnable runnable) {
        return this;
    }

    @Override
    public UserCommand onResume(Runnable runnable) {
        return this;
    }

    @Override
    public UserCommand onUnknownCommand(Consumer<String> consumer) {
        consumer.accept(wrongCommand);
        return this;
    }
}