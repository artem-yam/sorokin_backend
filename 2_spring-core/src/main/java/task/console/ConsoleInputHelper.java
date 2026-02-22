package task.console;

import org.springframework.stereotype.Component;
import task.model.command.CommandType;

import java.util.Scanner;

@Component
public class ConsoleInputHelper {

    private final Scanner scanner = new Scanner(System.in);

    public CommandType readCommand() {
        System.out.println("Введите операцию:");
        CommandType inputCommand;
        try {
            inputCommand = CommandType.valueOf(readText().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Операция указана неверно, повторите ввод.", e);
        }
        return inputCommand;
    }

    public String readText() {
        return scanner.nextLine().trim();
    }

    public int readId() {
        int value;
        try {
            value = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный формат для ID.", e);
        }
        checkForPositiveNumber(value);
        return value;
    }

    public double readAmount() {
        double value;
        try {
            value = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный формат для суммы.", e);
        }
        checkForPositiveNumber(value);
        return value;
    }

    private void checkForPositiveNumber(double number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Введенное число должна быть больше 0.");
        }
    }

}
