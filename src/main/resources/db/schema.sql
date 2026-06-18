CREATE DATABASE IF NOT EXISTS coinbet_simulator;

CREATE USER IF NOT EXISTS 'coinbet'@'localhost' IDENTIFIED BY 'coinbet_password';
GRANT ALL PRIVILEGES ON coinbet_simulator.* TO 'coinbet'@'localhost';
FLUSH PRIVILEGES;

USE coinbet_simulator;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    coin_balance INT NOT NULL
);

CREATE TABLE IF NOT EXISTS sports_games (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    sport VARCHAR(255) NOT NULL,
    home_team VARCHAR(255) NOT NULL,
    away_team VARCHAR(255) NOT NULL,
    home_odds DECIMAL(5, 2) NOT NULL,
    away_odds DECIMAL(5, 2) NOT NULL,
    start_time DATETIME(6) NOT NULL,
    status VARCHAR(255) NOT NULL,
    winner VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS bets (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    sports_game_id BIGINT NOT NULL,
    pick VARCHAR(10) NOT NULL,
    amount_wagered INT NOT NULL,
    outcome VARCHAR(255) NOT NULL,
    coins_won_lost INT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT fk_bets_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_bets_sports_game FOREIGN KEY (sports_game_id) REFERENCES sports_games (id)
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount INT NOT NULL,
    reason VARCHAR(80) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users (id)
);
