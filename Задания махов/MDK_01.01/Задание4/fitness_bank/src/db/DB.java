package db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Подключение к PostgreSQL.
 * <p>
 * Настройки (в порядке приоритета): переменные окружения {@code FITNESS_BANK_JDBC_URL},
 * {@code FITNESS_BANK_DB_USER}, {@code FITNESS_BANK_DB_PASSWORD}; затем файл {@code db.properties}
 * в каталоге запуска (в IntelliJ обычно это корень модуля {@code fitness_bank}); затем значения по умолчанию.
 * </p>
 * <p>
 * В pgAdmin 4: убедитесь, что запущен сервер PostgreSQL; создайте базу {@code fitness_bank};
 * откройте Query Tool и выполните скрипт {@code sql/init_fitness_bank.sql}.
 * Подробности — в файле {@code ПОДКЛЮЧЕНИЕ_БД.txt} в корне проекта.
 * </p>
 */
public class DB {
    private static final String CONFIG_FILE = "db.properties";

    private static final Properties FILE_PROPS = loadFileProperties();

    private static final String URL = firstNonBlank(
            System.getenv("FITNESS_BANK_JDBC_URL"),
            FILE_PROPS.getProperty("jdbc.url"),
            "jdbc:postgresql://localhost:5432/fitness_bank");

    private static final String USER = firstNonBlank(
            System.getenv("FITNESS_BANK_DB_USER"),
            FILE_PROPS.getProperty("db.user"),
            "postgres");

    private static final String PASSWORD = firstNonBlank(
            System.getenv("FITNESS_BANK_DB_PASSWORD"),
            FILE_PROPS.getProperty("db.password"),
            "");

    private static Properties loadFileProperties() {
        Properties p = new Properties();
        Path path = Paths.get(System.getProperty("user.dir"), CONFIG_FILE);
        if (!Files.isRegularFile(path)) {
            return p;
        }
        try (InputStream in = Files.newInputStream(path)) {
            p.load(in);
        } catch (IOException e) {
            System.err.println("Не удалось прочитать " + path.toAbsolutePath() + ": " + e.getMessage());
        }
        return p;
    }

    private static String firstNonBlank(String a, String b, String c) {
        if (a != null && !a.isBlank()) {
            return a.trim();
        }
        if (b != null && !b.isBlank()) {
            return b.trim();
        }
        return c != null ? c : "";
    }

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Драйвер PostgreSQL загружен");
        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка загрузки драйвера JDBC: " + e.getMessage());
            System.err.println("Положите postgresql-42.7.4.jar в папку lib/ проекта (см. ПОДКЛЮЧЕНИЕ_БД.txt).");
        }
    }

    public static java.sql.Connection getConnection() throws SQLException {
        if (PASSWORD.isEmpty()) {
            throw new SQLException(
                    "Пароль БД пустой. Укажите db.password в db.properties или переменную FITNESS_BANK_DB_PASSWORD.");
        }
        System.out.println("Подключение к базе данных...");
        java.sql.Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Подключение успешно.");
        return conn;
    }

    public static void testConnection() {
        Path cfg = Paths.get(System.getProperty("user.dir"), CONFIG_FILE).toAbsolutePath();
        if (!Files.isRegularFile(cfg)) {
            System.err.println("Файл настроек не найден: " + cfg);
            System.err.println("Скопируйте db.properties.example в db.properties и укажите пароль postgres.");
        }
        try (java.sql.Connection conn = getConnection()) {
            System.out.println("Тест подключения: успешно.");
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к базе данных.");
            System.err.println("Проверьте: сервер PostgreSQL запущен, база fitness_bank создана, скрипт init_fitness_bank.sql выполнен.");
            System.err.println("Параметры: URL=" + URL + ", USER=" + USER);
            System.err.println("Детали: " + e.getMessage());
            System.err.println("Пошагово: см. ПОДКЛЮЧЕНИЕ_БД.txt в папке проекта fitness_bank.");
        }
    }
}
