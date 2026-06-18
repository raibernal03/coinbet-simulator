package com.coinbet.simulator.service;

import com.coinbet.simulator.dto.BetDtos.BetResponse;
import com.coinbet.simulator.dto.BetDtos.GameResponse;
import com.coinbet.simulator.model.AppUser;
import com.coinbet.simulator.model.Bet;
import com.coinbet.simulator.model.BetOutcome;
import com.coinbet.simulator.model.GameStatus;
import com.coinbet.simulator.model.SportsGame;
import com.coinbet.simulator.repository.BetRepository;
import com.coinbet.simulator.repository.SportsGameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class BetService {

    private final SportsGameRepository sportsGameRepository;
    private final BetRepository betRepository;
    private final CoinService coinService;

    public BetService(SportsGameRepository sportsGameRepository, BetRepository betRepository, CoinService coinService) {
        this.sportsGameRepository = sportsGameRepository;
        this.betRepository = betRepository;
        this.coinService = coinService;
    }

    public List<GameResponse> listGames() {
        return sportsGameRepository.findAll().stream()
                .map(this::toGameResponse)
                .toList();
    }

    @Transactional
    public BetResponse placeBet(AppUser user, Long gameId, String pick, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Bet amount must be positive.");
        }
        if (user.getCoinBalance() < amount) {
            throw new IllegalArgumentException("Not enough Coins for that wager.");
        }

        String normalizedPick = pick.trim().toUpperCase();
        if (!normalizedPick.equals("HOME") && !normalizedPick.equals("AWAY")) {
            throw new IllegalArgumentException("Pick must be HOME or AWAY.");
        }

        SportsGame game = sportsGameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found."));

        coinService.recordTransaction(user, -amount, "Bet wager");

        BetOutcome outcome = BetOutcome.PENDING;
        int coinsWonLost = 0;
        if (game.getStatus() == GameStatus.FINISHED && game.getWinner() != null) {
            boolean won = normalizedPick.equals(game.getWinner());
            outcome = won ? BetOutcome.WON : BetOutcome.LOST;
            if (won) {
                BigDecimal odds = normalizedPick.equals("HOME") ? game.getHomeOdds() : game.getAwayOdds();
                int payout = odds.multiply(BigDecimal.valueOf(amount)).setScale(0, RoundingMode.DOWN).intValue();
                coinService.recordTransaction(user, payout, "Winning bet payout");
                coinsWonLost = payout - amount;
            } else {
                coinsWonLost = -amount;
            }
        }

        Bet bet = betRepository.save(new Bet(user, game, normalizedPick, amount, outcome, coinsWonLost));
        return toBetResponse(bet);
    }

    public List<BetResponse> getHistory(AppUser user) {
        return betRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::toBetResponse)
                .toList();
    }

    private GameResponse toGameResponse(SportsGame game) {
        return new GameResponse(
                game.getId(),
                game.getSport(),
                game.getHomeTeam(),
                game.getAwayTeam(),
                game.getHomeOdds(),
                game.getAwayOdds(),
                game.getStartTime(),
                game.getStatus(),
                game.getWinner()
        );
    }

    private BetResponse toBetResponse(Bet bet) {
        SportsGame game = bet.getSportsGame();
        return new BetResponse(
                bet.getId(),
                game.getHomeTeam() + " vs " + game.getAwayTeam(),
                bet.getPick(),
                bet.getAmountWagered(),
                bet.getOutcome(),
                bet.getCoinsWonLost(),
                bet.getCreatedAt()
        );
    }
}
