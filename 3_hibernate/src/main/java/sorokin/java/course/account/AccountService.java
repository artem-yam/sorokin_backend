package sorokin.java.course.account;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import sorokin.java.course.TransactionHelper;
import sorokin.java.course.user.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountService {

    private final AccountProperties accountProperties;
    private final TransactionHelper transactionHelper;

    public Account createAccount(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        return transactionHelper.executeInTransactionOrJoin(session -> {
            var managedUser = session.merge(user);
            Account newAccount = new Account(null, managedUser, accountProperties.getDefaultAmount());
            session.persist(newAccount);
            return newAccount;
        });
    }

    public Optional<Account> findAccountById(Integer id) {
        validatePositiveId(id, "account id");
        return Optional.ofNullable(transactionHelper.executeInTransactionOrJoin((session -> session.get(Account.class, id))));
    }

    public List<Account> getUserAccounts(Integer userId) {
        return transactionHelper.executeInTransactionOrJoin((session) -> {
            Query<Account> query = session.createQuery("FROM Account a WHERE a.owner.id = ?1", Account.class);
            query.setParameter(1, userId);
            return query.list();
        });
    }

    public void withdraw(Integer fromAccountId, Integer amount) {
        validatePositiveId(fromAccountId, "account id");
        validatePositiveAmount(amount);

        transactionHelper.executeInTransactionOrJoin((session) -> {
            Account account = findAccountById(fromAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(fromAccountId)));

            if (amount > account.getMoneyAmount()) {
                throw new IllegalArgumentException(
                        "insufficient funds on account id=%s, moneyAmount=%s, attempted withdraw=%s"
                                .formatted(account.getId(), account.getMoneyAmount(), amount)
                );
            }
            account.setMoneyAmount(account.getMoneyAmount() - amount);
            return account;
        });
    }

    public void deposit(Integer toAccountId, Integer amount) {
        validatePositiveId(toAccountId, "account id");
        validatePositiveAmount(amount);

        transactionHelper.executeInTransactionOrJoin((session) -> {
            Account account = findAccountById(toAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(toAccountId)));

            account.setMoneyAmount(account.getMoneyAmount() + amount);
            return account;
        });
    }

    public void closeAccount(Integer accountId) {
        validatePositiveId(accountId, "account id");

        transactionHelper.executeInTransactionOrJoin((session) -> {
            Account accountToClose = findAccountById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(accountId)));
            var userId = accountToClose.getOwner().getId();
            var userAccounts = getUserAccounts(userId);
            if (userAccounts.size() == 1) {
                throw new IllegalStateException("Can't close the only one account");
            }

            var accountToTransferMoney = userAccounts.stream()
                    .filter(it -> !Objects.equals(it.getId(), accountId))
                    .findFirst()
                    .orElseThrow();

            var newAmount = accountToTransferMoney.getMoneyAmount() + accountToClose.getMoneyAmount();
            accountToTransferMoney.setMoneyAmount(newAmount);
            session.remove(accountToClose);
            return accountToClose;
        });
    }

    public void transfer(int fromAccountId, int toAccountId, int amount) {
        validatePositiveId(fromAccountId, "source account id");
        validatePositiveId(toAccountId, "target account id");
        validatePositiveAmount(amount);
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("source and target account id must be different");
        }

        transactionHelper.executeInTransactionOrJoin((session) -> {
            Account accountFrom = findAccountById(fromAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(fromAccountId)));
            Account accountTo = findAccountById(toAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(toAccountId)));

            if (amount > accountFrom.getMoneyAmount()) {
                throw new IllegalArgumentException(
                        "insufficient funds on account id=%s, moneyAmount=%s, attempted transfer=%s"
                                .formatted(accountFrom.getId(), accountFrom.getMoneyAmount(), amount)
                );
            }
            accountFrom.setMoneyAmount(accountFrom.getMoneyAmount() - amount);

            int amountToTransfer = Objects.equals(accountTo.getOwner().getId(), accountFrom.getOwner().getId())
                    ? amount
                    : (int) Math.round(amount * (1 - accountProperties.getTransferCommission()));
            accountTo.setMoneyAmount(accountTo.getMoneyAmount() + amountToTransfer);

            return accountTo;
        });
    }

    private void validatePositiveId(Integer id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(fieldName + " must be > 0");
        }
    }

    private void validatePositiveAmount(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
    }
}
