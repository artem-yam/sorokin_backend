package sorokin.java.course.operations.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sorokin.java.course.operations.ConsoleOperationType;
import sorokin.java.course.operations.OperationCommand;
import sorokin.java.course.user.UserService;

@Component
@RequiredArgsConstructor
public class ShowAllUsersCommand implements OperationCommand {

    private final UserService userService;

    @Override
    public void execute() {
        System.out.println("List of all users:");
        userService.findAll().forEach(System.out::println);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }
}
