package task.model.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import task.console.ConsoleInputHelper;
import task.model.Account;
import task.service.AccountService;
import task.service.UserService;

@Component
@RequiredArgsConstructor
public class CloseAccountCommand implements ExecutableCommand {

    private final ConsoleInputHelper inputHelper;
    private final AccountService accountService;
    private final UserService userService;

    @Override
    public void execute() {
        System.out.println("Закрытие счета...");

        System.out.println("Введите ID счета:");
        int accId = inputHelper.readId();
        Account accountToClose = accountService.findById(accId);
        System.out.printf("Счет найден %s%n", accountToClose);

        Account reserveAccount = accountService.closeAccount(accountToClose);
        System.out.printf("Указанный счет закрыт. Остаток средств был перечислен на другой счет %s%n", reserveAccount);

        userService.unlinkAccount(accountToClose);
    }

    @Override
    public CommandType getType() {
        return CommandType.ACCOUNT_CLOSE;
    }
}
