package sorokin.java.course.operations.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sorokin.java.course.console.ConsoleInput;
import sorokin.java.course.operations.ConsoleOperationType;
import sorokin.java.course.operations.OperationCommand;
import sorokin.java.course.user.UserService;

@Component
@RequiredArgsConstructor
public class CreateUserCommand implements OperationCommand {

    private final UserService userService;
    private final ConsoleInput consoleInput;

    @Override
    public void execute() {
        String login = consoleInput.readRequiredString("Enter login:", "login");
        var user = userService.createUser(login);
        System.out.println("User created: " + user);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.USER_CREATE;
    }
}
