package task.console;

import org.springframework.stereotype.Component;
import task.model.command.CommandType;
import task.model.command.ExecutableCommand;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConsoleListener {

    private final ConsoleInputHelper inputHelper;
    private final Map<CommandType, ExecutableCommand> commands;

    public ConsoleListener(ConsoleInputHelper inputHelper, List<ExecutableCommand> commands) {
        this.inputHelper = inputHelper;
        this.commands = commands.stream().collect(Collectors.toMap(ExecutableCommand::getType, command -> command));
    }

    public void run() {
        System.out.println("Банк запущен.");
        System.out.printf("Доступные команды: %s%n", commands.keySet());

        while (true) {
            CommandType inputCommand = inputHelper.readCommand();
            try {
                ExecutableCommand command = commands.get(inputCommand);
                command.execute();
                if (CommandType.EXIT == command.getType()) {
                    break;
                }
            } catch (NullPointerException e) {
                System.out.println("Операция не реализована, повторите ввод.");
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }

        }
    }


}
