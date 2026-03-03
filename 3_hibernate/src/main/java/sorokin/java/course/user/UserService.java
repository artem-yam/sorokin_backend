package sorokin.java.course.user;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import sorokin.java.course.TransactionHelper;
import sorokin.java.course.account.AccountService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserService {

    private final AccountService accountService;
    private final TransactionHelper transactionHelper;

    public User createUser(String login) {
        String normalizedLogin = validateLogin(login);

        var logins = getAllLogins();
        if (logins.contains(normalizedLogin)) {
            throw new IllegalArgumentException("User already exists with login=%s".formatted(normalizedLogin));
        }

        return transactionHelper.executeInTransactionOrJoin(session -> {
            var newUser = new User(null, normalizedLogin, null);
            session.persist(newUser);
            var newAccount = accountService.createAccount(newUser);
            newUser.setAccounts(List.of(newAccount));

            return newUser;
        });
    }

    public User findUserById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("user id must be > 0");
        }
        var user = transactionHelper.executeInTransactionOrJoin((session -> session.get(User.class, id)));
        if (user == null) {
            throw new IllegalArgumentException("No such user with id=%s".formatted(id));
        }
        return user;
    }

    public List<User> findAll() {
        return transactionHelper.executeInTransactionOrJoin((session) -> {
            Query<User> query = session.createQuery("FROM User u join fetch u.accounts", User.class);
            return query.list();
        });
    }

    private List<String> getAllLogins() {
        return transactionHelper.executeInTransactionOrJoin((session) -> {
            Query<String> query = session.createQuery("SELECT u.login FROM User u", String.class);
            return query.list();
        });
    }

    private String validateLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("login must not be blank");
        }
        return login.trim();
    }
}
