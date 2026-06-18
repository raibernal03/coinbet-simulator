const messageEl = document.querySelector("#message");

function showMessage(text) {
    if (messageEl) {
        messageEl.textContent = text || "";
    }
}

async function api(path, options = {}) {
    const response = await fetch(path, {
        credentials: "same-origin",
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    if (response.status === 401 && !location.pathname.endsWith("/login.html")) {
        location.href = "/login.html";
        return null;
    }

    if (!response.ok) {
        let message = response.statusText;
        try {
            const error = await response.json();
            message = error.message || message;
        } catch {
            // Some Spring Security errors are plain responses.
        }
        throw new Error(message);
    }

    if (response.status === 204) {
        return null;
    }
    return response.json();
}

function formatDate(value) {
    return value ? new Date(value).toLocaleString() : "";
}

async function loadMe() {
    const user = await api("/api/auth/me");
    if (!user) {
        return null;
    }
    const username = document.querySelector("#username");
    const balance = document.querySelector("#coin-balance");
    if (username) {
        username.textContent = user.username;
    }
    if (balance) {
        balance.textContent = user.coinBalance;
    }
    return user;
}

async function loadTransactions() {
    const body = document.querySelector("#transactions-body");
    if (!body) {
        return;
    }
    const transactions = await api("/api/coins/transactions");
    body.innerHTML = transactions.map(transaction => `
        <tr>
            <td>${transaction.amount}</td>
            <td>${transaction.reason}</td>
            <td>${formatDate(transaction.createdAt)}</td>
        </tr>
    `).join("");
}

async function loadGames() {
    const gamesList = document.querySelector("#games-list");
    if (!gamesList) {
        return;
    }
    const games = await api("/api/games");
    gamesList.innerHTML = games.map(game => `
        <article class="game-card">
            <h3>${game.sport}: ${game.homeTeam} vs ${game.awayTeam}</h3>
            <p>Status: ${game.status} ${game.winner ? `(Winner: ${game.winner})` : ""}</p>
            <p>Home odds: ${game.homeOdds} | Away odds: ${game.awayOdds}</p>
            <p>Start: ${formatDate(game.startTime)}</p>
            <form class="bet-form" data-game-id="${game.id}">
                <label>Pick
                    <select name="pick">
                        <option value="HOME">${game.homeTeam}</option>
                        <option value="AWAY">${game.awayTeam}</option>
                    </select>
                </label>
                <label>Coins
                    <input name="amount" type="number" min="1" value="10" required>
                </label>
                <button type="submit">Place Bet</button>
            </form>
        </article>
    `).join("");

    document.querySelectorAll(".bet-form").forEach(form => {
        form.addEventListener("submit", async event => {
            event.preventDefault();
            const data = new FormData(form);
            try {
                const bet = await api("/api/bets", {
                    method: "POST",
                    body: JSON.stringify({
                        gameId: Number(form.dataset.gameId),
                        pick: data.get("pick"),
                        amount: Number(data.get("amount"))
                    })
                });
                showMessage(`Bet ${bet.outcome.toLowerCase()}. Coin result: ${bet.coinsWonLost}.`);
                await loadMe();
                await loadBetHistory();
            } catch (error) {
                showMessage(error.message);
            }
        });
    });
}

async function loadBetHistory() {
    const body = document.querySelector("#bets-body");
    if (!body) {
        return;
    }
    const bets = await api("/api/bets/history");
    body.innerHTML = bets.map(bet => `
        <tr>
            <td>${bet.game}</td>
            <td>${bet.pick}</td>
            <td>${bet.amountWagered}</td>
            <td>${bet.outcome}</td>
            <td>${bet.coinsWonLost}</td>
            <td>${formatDate(bet.createdAt)}</td>
        </tr>
    `).join("");
}

function setupAuthForms() {
    const loginForm = document.querySelector("#login-form");
    const registerForm = document.querySelector("#register-form");

    if (loginForm) {
        loginForm.addEventListener("submit", async event => {
            event.preventDefault();
            const data = new FormData(loginForm);
            try {
                await fetch("/api/auth/login", {
                    method: "POST",
                    credentials: "same-origin",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
                    body: new URLSearchParams(data)
                }).then(response => {
                    if (!response.ok) {
                        throw new Error("Invalid username or password.");
                    }
                });
                location.href = "/index.html";
            } catch (error) {
                showMessage(error.message);
            }
        });
    }

    if (registerForm) {
        registerForm.addEventListener("submit", async event => {
            event.preventDefault();
            const data = new FormData(registerForm);
            try {
                await api("/api/auth/register", {
                    method: "POST",
                    body: JSON.stringify({
                        username: data.get("username"),
                        password: data.get("password")
                    })
                });
                showMessage("Account created. You can log in now.");
                registerForm.reset();
            } catch (error) {
                showMessage(error.message);
            }
        });
    }
}

function setupCommonActions() {
    const logoutButton = document.querySelector("#logout-button");
    const watchAdButton = document.querySelector("#watch-ad-button");

    if (logoutButton) {
        logoutButton.addEventListener("click", async () => {
            await fetch("/api/auth/logout", {method: "POST", credentials: "same-origin"});
            location.href = "/login.html";
        });
    }

    if (watchAdButton) {
        watchAdButton.addEventListener("click", async () => {
            try {
                const result = await api("/api/coins/watch-ad", {method: "POST"});
                showMessage(`${result.message} +${result.reward} Coins.`);
                await loadMe();
                await loadTransactions();
            } catch (error) {
                showMessage(error.message);
            }
        });
    }
}

function setupTicTacToe() {
    const boardEl = document.querySelector("#tic-board");
    const resetButton = document.querySelector("#reset-tic-button");
    if (!boardEl) {
        return;
    }

    let board = Array(9).fill("");
    let gameOver = false;
    const wins = [
        [0, 1, 2], [3, 4, 5], [6, 7, 8],
        [0, 3, 6], [1, 4, 7], [2, 5, 8],
        [0, 4, 8], [2, 4, 6]
    ];

    function winner() {
        for (const [a, b, c] of wins) {
            if (board[a] && board[a] === board[b] && board[a] === board[c]) {
                return board[a];
            }
        }
        return board.every(Boolean) ? "DRAW" : "";
    }

    async function finish(result) {
        gameOver = true;
        const won = result === "X";
        if (won) {
            const reward = await api("/api/minigames/result", {
                method: "POST",
                body: JSON.stringify({gameType: "tic-tac-toe", won: true})
            });
            showMessage(`You won Tic Tac Toe. +${reward.reward} Coins.`);
            await loadMe();
        } else {
            await api("/api/minigames/result", {
                method: "POST",
                body: JSON.stringify({gameType: "tic-tac-toe", won: false})
            });
            showMessage(result === "DRAW" ? "Draw. No Coins awarded." : "Computer won. No Coins awarded.");
        }
    }

    function computerMove() {
        const open = board.map((value, index) => value ? null : index).filter(index => index !== null);
        if (open.length > 0) {
            board[open[0]] = "O";
        }
    }

    function render() {
        boardEl.innerHTML = board.map((value, index) => `
            <button class="tic-cell" type="button" data-index="${index}">${value}</button>
        `).join("");

        document.querySelectorAll(".tic-cell").forEach(cell => {
            cell.addEventListener("click", async () => {
                const index = Number(cell.dataset.index);
                if (gameOver || board[index]) {
                    return;
                }
                board[index] = "X";
                let result = winner();
                if (!result) {
                    computerMove();
                    result = winner();
                }
                render();
                if (result) {
                    await finish(result);
                }
            });
        });
    }

    function reset() {
        board = Array(9).fill("");
        gameOver = false;
        showMessage("");
        render();
    }

    if (resetButton) {
        resetButton.addEventListener("click", reset);
    }
    reset();
}

function setupClicker() {
    const start = document.querySelector("#start-clicker-button");
    const target = document.querySelector("#click-target-button");
    const timeEl = document.querySelector("#clicker-time");
    const scoreEl = document.querySelector("#clicker-score");
    if (!start || !target || !timeEl || !scoreEl) {
        return;
    }

    let score = 0;
    let time = 10;
    let timerId = null;

    async function endGame() {
        clearInterval(timerId);
        target.disabled = true;
        start.disabled = false;
        const won = score >= 10;
        const reward = await api("/api/minigames/result", {
            method: "POST",
            body: JSON.stringify({gameType: "arcade-clicker", won})
        });
        showMessage(won ? `Clicker won. +${reward.reward} Coins.` : "Clicker lost. No Coins awarded.");
        await loadMe();
    }

    start.addEventListener("click", () => {
        score = 0;
        time = 10;
        scoreEl.textContent = score;
        timeEl.textContent = time;
        target.disabled = false;
        start.disabled = true;
        showMessage("");

        timerId = setInterval(async () => {
            time -= 1;
            timeEl.textContent = time;
            if (time <= 0) {
                await endGame();
            }
        }, 1000);
    });

    target.addEventListener("click", async () => {
        score += 1;
        scoreEl.textContent = score;
        if (score >= 10) {
            await endGame();
        }
    });
}

document.addEventListener("DOMContentLoaded", async () => {
    setupAuthForms();
    setupCommonActions();
    setupTicTacToe();
    setupClicker();

    if (!location.pathname.endsWith("/login.html")) {
        try {
            await loadMe();
            await loadTransactions();
            await loadGames();
            await loadBetHistory();
        } catch (error) {
            showMessage(error.message);
        }
    }
});
