import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Класс для регистрации пользователей с проверкой логина и пароля.
 * Хранит зарегистрированных пользователей и позволяет вывести их список.
 */
public class Registration {

    // Список для хранения зарегистрированных пользователей (логин и пароль)
    private static List<User> users = new ArrayList<>();

    // Паттерн для проверки допустимых символов: латинские буквы, цифры, подчеркивание
    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

    /**
     * Внутренний класс для представления пользователя.
     */
    private static class User {
        String login;
        String password;

        User(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Override
        public String toString() {
            return "Логин: " + login + ", Пароль: " + password;
        }
    }

    /**
     * Исключение, выбрасываемое при ошибках, связанных с логином.
     */
    public static class LoginException extends Exception {
        public LoginException(String message) {
            super(message);
        }
    }

    /**
     * Исключение, выбрасываемое при ошибках, связанных с паролем.
     */
    public static class PasswordException extends Exception {
        public PasswordException(String message) {
            super(message);
        }
    }

    /**
     * Метод регистрации нового пользователя.
     *
     * @param login           логин пользователя
     * @param password        пароль
     * @param confirmPassword подтверждение пароля
     * @throws LoginException    если логин не соответствует требованиям
     * @throws PasswordException если пароль не соответствует требованиям или не совпадает с подтверждением
     */
    public static void register(String login, String password, String confirmPassword)
            throws LoginException, PasswordException {

        // Проверка логина
        if (login == null || login.isEmpty()) {
            throw new LoginException("Логин не может быть пустым");
        }
        if (login.length() >= 15) {
            throw new LoginException("Длина логина должна быть меньше 15 символов");
        }
        if (!VALID_PATTERN.matcher(login).matches()) {
            throw new LoginException("Логин может содержать только латинские буквы, цифры и знак подчеркивания");
        }

        // Проверка пароля
        if (password == null || password.isEmpty()) {
            throw new PasswordException("Пароль не может быть пустым");
        }
        if (password.length() < 7 || password.length() > 20) {
            throw new PasswordException("Пароль должен содержать от 7 до 20 символов");
        }
        if (!VALID_PATTERN.matcher(password).matches()) {
            throw new PasswordException("Пароль может содержать только латинские буквы, цифры и знак подчеркивания");
        }

        // Проверка совпадения пароля и подтверждения
        if (!password.equals(confirmPassword)) {
            throw new PasswordException("Пароль и подтверждение не совпадают");
        }

        // Если все проверки пройдены, сохраняем пользователя
        users.add(new User(login, password));
        System.out.println("Пользователь " + login + " успешно зарегистрирован.");
    }

    /**
     * Выводит в консоль список всех сохраненных логинов и паролей.
     */
    public static void printAllUsers() {
        if (users.isEmpty()) {
            System.out.println("Список пользователей пуст.");
        } else {
            System.out.println("Список зарегистрированных пользователей:");
            for (User user : users) {
                System.out.println(user);
            }
        }
    }

    /**
     * Демонстрация работы методов.
     */
    public static void main(String[] args) {
        // Примеры корректной регистрации
        try {
            register("john_doe", "pass1234", "pass1234");
            register("alice_99", "my_password", "my_password");
        } catch (LoginException | PasswordException e) {
            System.err.println("Ошибка регистрации: " + e.getMessage());
        }

        // Пример с ошибкой в логине (слишком длинный)
        try {
            register("very_long_login_12345", "qwerty123", "qwerty123");
        } catch (LoginException | PasswordException e) {
            System.err.println("Ошибка регистрации: " + e.getMessage());
        }

        // Пример с недопустимыми символами в логине
        try {
            register("john@doe", "validPass", "validPass");
        } catch (LoginException | PasswordException e) {
            System.err.println("Ошибка регистрации: " + e.getMessage());
        }

        // Пример с коротким паролем
        try {
            register("shortpass", "123", "123");
        } catch (LoginException | PasswordException e) {
            System.err.println("Ошибка регистрации: " + e.getMessage());
        }

        // Пример с несовпадением паролей
        try {
            register("mismatch", "pass1234", "pass5678");
        } catch (LoginException | PasswordException e) {
            System.err.println("Ошибка регистрации: " + e.getMessage());
        }

        // Вывод всех сохраненных пользователей
        System.out.println();
        printAllUsers();
    }
}