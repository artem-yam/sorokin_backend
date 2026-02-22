package task.model.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import task.console.ConsoleInputHelper;
import task.model.Account;
import task.service.AccountService;

@Component
@RequiredArgsConstructor
public class DepositAccountCommand implements ExecutableCommand {

    private final ConsoleInputHelper inputHelper;
    private final AccountService accountService;

    @Override
    public void execute() {
        System.out.println("Пополнение счета...");

        System.out.println("Введите ID счета:");
        int accId = inputHelper.readId();
        Account account = accountService.findById(accId);
        System.out.printf("Счет найден %s%n", account);

        System.out.println("Укажите сумму для зачисления:");
        double depositAmount = inputHelper.readAmount();

        account = accountService.depositToAccount(account, depositAmount);
        System.out.printf("Сумма зачислена на счет %s%n", account);
    }

    @Override
    public CommandType getType() {
        return CommandType.ACCOUNT_DEPOSIT;
    }
}
