package task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import task.model.Account;
import task.model.User;

import java.util.*;

@Component
@RequiredArgsConstructor
public class UserService {

    private final Map<Integer, User> usersById = new HashMap<>();
    private final Set<String> takenLogins = new HashSet<>();

    private final AccountService accountService;
    private int idCounter;

    public User findById(int userId) {
        var user = usersById.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с таким ID не найден");
        }
        return user;
    }

    public User createUser(String login) {
        checkExistingLogin(login);

        User newUser = new User(++idCounter, login, new ArrayList<>());
        accountService.createAccount(newUser);

        usersById.put(newUser.getId(), newUser);
        takenLogins.add(login);
        return newUser;
    }

    public Iterable<User> getAll() {
        return usersById.values();
    }

    public void unlinkAccount(Account account) {
        usersById.get(account.getUserId()).getAccountList().remove(account);
    }

    private void checkExistingLogin(String login) {
        if (takenLogins.contains(login)) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }
    }

}
