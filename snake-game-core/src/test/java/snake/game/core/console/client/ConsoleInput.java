package snake.game.core.console.client;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;

import java.util.Scanner;

@AllArgsConstructor
class ConsoleInput {
    private final Scanner scanner;

    void waitForEnterPress() {
        new Scanner(System.in).nextLine();
    }

    Try<Integer> getInt() {
        return Try.of(scanner::nextInt);
    }

    String getString() {
        return scanner.nextLine();
    }
}