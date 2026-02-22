package task.model.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import task.service.UserService;

@Component
@RequiredArgsConstructor
public class ShowUsersCommand implements ExecutableCommand {

    private final UserService userService;

    @Override
    public void execute() {
        System.out.println("Список пользователей:");
        userService.getAll().forEach(System.out::println);
    }

    @Override
    public CommandType getType() {
        return CommandType.SHOW_ALL_USERS;
    }
}
