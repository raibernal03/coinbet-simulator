# CoinBet Simulator

CoinBet Simulator is a full-stack Spring Boot application for simulated sports betting with fictional Coins only. It does not include real-money gambling, deposits, withdrawals, payment processing, or live sports APIs.

## Technology Stack

- Java 17
- Spring Boot MVC with embedded Apache Tomcat
- Maven
- Spring Data JPA / Hibernate
- Spring Security with BCrypt password hashing and session login
- MariaDB
- Basic HTML, CSS, and vanilla JavaScript

## Project Structure

```text
coinbet-simulator/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/coinbet/simulator/
        │   ├── CoinBetSimulatorApplication.java
        │   ├── controller/
        │   ├── dto/
        │   ├── model/
        │   ├── repository/
        │   ├── security/
        │   └── service/
        └── resources/
            ├── application.properties
            ├── db/schema.sql
            └── static/
                ├── app.js
                ├── betting.html
                ├── index.html
                ├── login.html
                ├── minigames.html
                └── styles.css
```

## Core Behavior

- New users start with 1,000 Coins.
- Users can register and log in with a username and password.
- Passwords are stored as BCrypt hashes.
- Mock sports games are seeded automatically on first startup.
- Users can place virtual Coin bets on HOME or AWAY teams.
- Finished mock games resolve immediately.
- Scheduled games create pending bets.
- Users can earn Coins from a simulated ad button.
- Users can earn Coins from Tic Tac Toe and Arcade Clicker mini-games.
- Coin transactions and betting history are stored in MariaDB.

## REST Endpoints

Authentication:

- `POST /api/auth/register` with JSON `{ "username": "demo", "password": "secret123" }`
- `POST /api/auth/login` with form fields `username` and `password`
- `POST /api/auth/logout`
- `GET /api/auth/me`

Coins:

- `GET /api/coins/balance`
- `POST /api/coins/watch-ad`
- `GET /api/coins/transactions`

Betting:

- `GET /api/games`
- `POST /api/bets` with JSON `{ "gameId": 1, "pick": "HOME", "amount": 10 }`
- `GET /api/bets/history`

Mini-games:

- `POST /api/minigames/result` with JSON `{ "gameType": "tic-tac-toe", "won": true }`
- `POST /api/minigames/result` with JSON `{ "gameType": "arcade-clicker", "won": true }`

## Database Setup

Log in to MariaDB as an admin user and run:

```sql
SOURCE src/main/resources/db/schema.sql;
```

Or manually create the database and user:

```sql
CREATE DATABASE coinbet_simulator;
CREATE USER 'coinbet'@'localhost' IDENTIFIED BY 'coinbet_password';
GRANT ALL PRIVILEGES ON coinbet_simulator.* TO 'coinbet'@'localhost';
FLUSH PRIVILEGES;
```

The application uses these defaults in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/coinbet_simulator
spring.datasource.username=coinbet
spring.datasource.password=coinbet_password
spring.jpa.hibernate.ddl-auto=update
```

## Run Locally

1. Install Java 17, Maven, and MariaDB.
2. Start MariaDB.
3. Create the database and user using `src/main/resources/db/schema.sql`.
4. From the project root, run:

```bash
mvn spring-boot:run
```

5. Open:

```text
http://localhost:8080/login.html
```

## Frontend Pages

- `login.html`: register and login forms
- `index.html`: dashboard, balance, simulated ad reward, transaction history
- `betting.html`: mock game odds, betting interface, betting history
- `minigames.html`: Tic Tac Toe and Arcade Clicker

The HTML and CSS are intentionally basic so they can be customized later.
