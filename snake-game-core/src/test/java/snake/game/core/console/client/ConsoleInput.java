package snake.game.core.console.client;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

import java.util.Scanner;

import static snake.game.core.dto.Direction.*;

@AllArgsConstructor
class ConsoleInput {
    private final Scanner scanner;

    void waitForEnterPress() {
        new Scanner(System.in).nextLine();
    }

    Option<Integer> getInt() {
        return Try
                .of(scanner::nextInt)
                .toOption();
    }
    
    String getString() {
        return scanner.nextLine();
    }
}