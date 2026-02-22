package task.model.command;

public enum CommandType {

    /**
     * Создание нового пользователя
     */
    USER_CREATE,

    /**
     * Отображение списка всех пользователей
     */
    SHOW_ALL_USERS,

    /**
     * Создание нового счёта для пользователя
     */
    ACCOUNT_CREATE,

    /**
     * Пополнение счёта
     */
    ACCOUNT_DEPOSIT,

    /**
     * Снятие средств со счёта
     */
    ACCOUNT_WITHDRAW,

    /**
     * Перевод средств между счетами
     */
    ACCOUNT_TRANSFER,

    /**
     * Закрытие счёта
     */
    ACCOUNT_CLOSE,

    /**
     * Завершение работы программы
     */
    EXIT
}
