package task.model.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExitCommand implements ExecutableCommand {

    @Override
    public void execute() {
        System.out.println("Завершается работа программы...");
    }

    @Override
    public CommandType getType() {
        return CommandType.EXIT;
    }
}
