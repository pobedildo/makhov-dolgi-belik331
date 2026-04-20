package db.service;

import db.DB;
import db.dao.AccountDAO;
import db.dao.TransactionDAO;
import db.models.DBAccount;
import db.models.DBTransaction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

// Исключения
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) { super(message); }
}

class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) { super(message); }
}

public class TransferService {
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    public void transferMoney(int fromAccountId, int toAccountId, BigDecimal amount, String description)
            throws SQLException, InsufficientFundsException, AccountNotFoundException {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть положительной");
        }

        Connection conn = null;
        try {
            System.out.println("🔄 Начинаем транзакцию перевода...");
            conn = DB.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            System.out.println("   Блокировка счета отправителя...");
            DBAccount fromAccount = accountDAO.getAccountByIdForUpdate(conn, fromAccountId);
            if (fromAccount == null) {
                throw new AccountNotFoundException("Счет отправителя не найден");
            }

            System.out.println("   Блокировка счета получателя...");
            DBAccount toAccount = accountDAO.getAccountByIdForUpdate(conn, toAccountId);
            if (toAccount == null) {
                throw new AccountNotFoundException("Счет получателя не найден");
            }

            System.out.println("   Проверка баланса...");
            System.out.println("      Баланс отправителя: " + fromAccount.getBalance() + " руб.");
            System.out.println("      Сумма перевода: " + amount + " руб.");

            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Недостаточно средств");
            }

            System.out.println("   Обновление балансов...");
            BigDecimal newFromBalance = fromAccount.getBalance().subtract(amount);
            BigDecimal newToBalance = toAccount.getBalance().add(amount);

            accountDAO.updateAccountBalance(conn, fromAccountId, newFromBalance);
            accountDAO.updateAccountBalance(conn, toAccountId, newToBalance);

            System.out.println("   Запись транзакции в историю...");
            DBTransaction transaction = new DBTransaction(fromAccountId, toAccountId, amount, description);
            transactionDAO.createTransaction(conn, transaction);

            conn.commit();
            System.out.println("✅ Перевод выполнен успешно! Сумма: " + amount + " руб.");

        } catch (SQLException | InsufficientFundsException | AccountNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("❌ Транзакция отменена. Выполнен ROLLBACK.");
                } catch (SQLException ex) {
                    System.err.println("❌ Ошибка при откате транзакции: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}