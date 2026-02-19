package task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import task.config.AccountProperties;
import task.model.Account;
import task.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountService {

    private final Map<Integer, Account> accountsById = new HashMap<>();
    private final AccountProperties accountProperties;
    private int idCounter;

    public Account createAccount(User user) {
        var newAccount = new Account(++idCounter, user.getId(), accountProperties.getDefaultAmount());
        accountsById.put(newAccount.getId(), newAccount);
        user.getAccountList().add(newAccount);
        return newAccount;
    }

    public Account findById(int accId) {
        var account = accountsById.get(accId);
        if (account == null) {
            throw new IllegalArgumentException("Счет с таким ID не найден");
        }
        return account;
    }

    public Account depositToAccount(Account account, double amount) {
        account.setMoneyAmount(account.getMoneyAmount() + amount);
        return account;
    }

    public Account withdrawFromAccount(Account account, double amount) {
        if (account.getMoneyAmount() < amount) {
            throw new IllegalArgumentException("На счете не достаточно средств.");
        }

        account.setMoneyAmount(account.getMoneyAmount() - amount);
        return account;
    }

    public Account closeAccount(Account account) {
        var otherAccount = getUserAnyOtherAccount(account);
        if (otherAccount.isPresent()) {
            depositToAccount(otherAccount.get(), account.getMoneyAmount());
            accountsById.remove(account.getId());
        } else {
            throw new IllegalArgumentException("Нельзя закрыть счет. У пользователя должны быть другие счета.");
        }

        return otherAccount.get();
    }

    public void transfer(Account sourceAccount, Account targetAccount, double amount) {
        if (sourceAccount.getMoneyAmount() < amount) {
            throw new IllegalArgumentException("На счете не достаточно средств.");
        } else {
            double transferCoefficient = sourceAccount.getUserId() == targetAccount.getUserId() ? 1 : (1 - accountProperties.getTransferCommission());

            sourceAccount.setMoneyAmount(sourceAccount.getMoneyAmount() - amount);
            targetAccount.setMoneyAmount(targetAccount.getMoneyAmount() + amount * transferCoefficient);
        }
    }

    private Optional<Account> getUserAnyOtherAccount(Account account) {
        return accountsById.values().stream()
                .filter(acc -> acc.getUserId() == account.getUserId() && acc.getId() != account.getId()).findFirst();
    }


}
