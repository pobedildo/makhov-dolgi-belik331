-- Скрипт для pgAdmin 4: Query Tool → открыть файл → Execute (F5).
-- Сначала создайте пустую базу данных с именем fitness_bank
-- (объект Databases → Create → Database → Name: fitness_bank).

DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS memberships CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id              SERIAL PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    birth_year      INTEGER      NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE memberships (
    id               SERIAL PRIMARY KEY,
    user_id          INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    membership_type  VARCHAR(50) NOT NULL,
    start_date       DATE NOT NULL,
    end_date         DATE NOT NULL,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
    id                      SERIAL PRIMARY KEY,
    user_id                 INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    account_number          VARCHAR(32) NOT NULL UNIQUE,
    balance                 NUMERIC(15, 2) NOT NULL DEFAULT 0,
    client_level            VARCHAR(20) NOT NULL,
    welcome_bonus_received  BOOLEAN NOT NULL DEFAULT FALSE,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
    id               SERIAL PRIMARY KEY,
    from_account_id  INTEGER REFERENCES accounts (id) ON DELETE SET NULL,
    to_account_id    INTEGER REFERENCES accounts (id) ON DELETE SET NULL,
    amount           NUMERIC(15, 2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description      VARCHAR(500)
);

CREATE INDEX idx_memberships_user_id ON memberships (user_id);
CREATE INDEX idx_accounts_user_id ON accounts (user_id);
CREATE INDEX idx_transactions_from ON transactions (from_account_id);
CREATE INDEX idx_transactions_to ON transactions (to_account_id);

-- Тестовые данные: есть «Петров» для поиска, два счёта с балансом для перевода 5000, VIP-счёт для пункта меню
INSERT INTO users (first_name, last_name, birth_year) VALUES
    ('Иван', 'Петров', 1990),
    ('Мария', 'Сидорова', 1995),
    ('Алексей', 'VIPКлиент', 1988);

INSERT INTO accounts (user_id, account_number, balance, client_level, welcome_bonus_received) VALUES
    (1, '40817810000000000001', 50000.00, 'STANDARD', TRUE),
    (2, '40817810000000000002', 30000.00, 'STANDARD', TRUE),
    (3, '40817810000000000003', 150000.00, 'VIP', FALSE);

INSERT INTO memberships (user_id, membership_type, start_date, end_date) VALUES
    (1, 'FULL', CURRENT_DATE, CURRENT_DATE + INTERVAL '1 month');
