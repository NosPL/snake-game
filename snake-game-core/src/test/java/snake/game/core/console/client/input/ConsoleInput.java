package snake.game.core.console.client.input;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

import java.util.Scanner;

import static snake.game.core.dto.Direction.*;

@AllArgsConstructor
public class ConsoleInput {
    private final Scanner scanner;

    public void waitForEnterPress() {
        new Scanner(System.in).nextLine();
    }

    public Option<Integer> getInt() {
        return Try
                .of(scanner::nextInt)
                .toOption();
    }

    public UserCommand getUserCommand() {
        var s = scanner.nextLine();
        if ("w".equalsIgnoreCase(s))
            return UserCommand.changeDirection(UP);
        if ("s".equalsIgnoreCase(s))
            return UserCommand.changeDirection(DOWN);
        if ("a".equalsIgnoreCase(s))
            return UserCommand.changeDirection(LEFT);
        if ("d".equalsIgnoreCase(s))
            return UserCommand.changeDirection(RIGHT);
        if ("q".equalsIgnoreCase(s))
            return UserCommand.exitCommand();
        if ("p".equalsIgnoreCase(s))
            return UserCommand.pauseCommand();
        if ("r".equalsIgnoreCase(s))
            return UserCommand.resumeCommand();
        return UserCommand.unknownCommand(s);
    }
}