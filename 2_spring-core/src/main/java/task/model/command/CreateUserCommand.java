package task.model.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import task.console.ConsoleInputHelper;
import task.service.UserService;

@Component
@RequiredArgsConstructor
public class CreateUserCommand implements ExecutableCommand {

    private final ConsoleInputHelper inputHelper;
    private final UserService userService;

    @Override
    public void execute() {
        System.out.println("Создание пользователя...");

        System.out.println("Введите логин:");
        var login = inputHelper.readText();

        var createdUser = userService.createUser(login);
        System.out.printf("Создан пользователь : %s%n", createdUser);
    }

    @Override
    public CommandType getType() {
        return CommandType.USER_CREATE;
    }
}
