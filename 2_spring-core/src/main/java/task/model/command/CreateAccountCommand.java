package task.model.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import task.console.ConsoleInputHelper;
import task.model.Account;
import task.service.AccountService;
import task.service.UserService;

@Component
@RequiredArgsConstructor
public class CreateAccountCommand implements ExecutableCommand {

    private final ConsoleInputHelper inputHelper;
    private final UserService userService;
    private final AccountService accountService;

    @Override
    public void execute() {
        System.out.println("Создание нового счета...");

        System.out.println("Введите ID пользователя:");
        int userId = inputHelper.readId();

        var userForAccount = userService.findById(userId);
        Account newAccount = accountService.createAccount(userForAccount);
        System.out.printf("Создан счет : %s%n", newAccount);
    }

    @Override
    public CommandType getType() {
        return CommandType.ACCOUNT_CREATE;
    }
}
