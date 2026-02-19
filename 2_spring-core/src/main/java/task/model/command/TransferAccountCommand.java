package task.model.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import task.console.ConsoleInputHelper;
import task.model.Account;
import task.service.AccountService;

@Component
@RequiredArgsConstructor
public class TransferAccountCommand implements ExecutableCommand {

    private final ConsoleInputHelper inputHelper;
    private final AccountService accountService;

    @Override
    public void execute() {
        System.out.println("Перевод средств между счетами...");

        System.out.println("Введите ID счета отправителя:");
        int sourceAccId = inputHelper.readId();
        Account sourceAccount = accountService.findById(sourceAccId);
        System.out.printf("Счет найден %s%n", sourceAccount);

        System.out.println("Введите ID счета получателя:");
        int targetAccId = inputHelper.readId();
        Account targetAccount = accountService.findById(targetAccId);
        System.out.printf("Счет найден %s%n", targetAccount);

        System.out.println("Укажите сумму для перевода:");
        double amount = inputHelper.readAmount();

        accountService.transfer(sourceAccount, targetAccount, amount);
        System.out.println("Перевод выполнен:");
        System.out.printf("Счет отправителя %s%n", sourceAccount);
        System.out.printf("Счет получателя %s%n", targetAccount);
    }

    @Override
    public CommandType getType() {
        return CommandType.ACCOUNT_TRANSFER;
    }
}
