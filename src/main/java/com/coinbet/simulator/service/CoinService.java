package com.coinbet.simulator.service;

import com.coinbet.simulator.dto.CoinDtos.RewardResponse;
import com.coinbet.simulator.dto.CoinDtos.TransactionResponse;
import com.coinbet.simulator.model.AppUser;
import com.coinbet.simulator.model.CoinTransaction;
import com.coinbet.simulator.repository.AppUserRepository;
import com.coinbet.simulator.repository.CoinTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CoinService {

    private static final int AD_REWARD = 10;

    private final AppUserRepository appUserRepository;
    private final CoinTransactionRepository transactionRepository;

    public CoinService(AppUserRepository appUserRepository, CoinTransactionRepository transactionRepository) {
        this.appUserRepository = appUserRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public RewardResponse rewardForAd(AppUser user) {
        return addCoins(user, AD_REWARD, "Simulated ad reward", "Thanks for watching the simulated ad.");
    }

    @Transactional
    public RewardResponse rewardForMiniGame(AppUser user, String gameType, boolean won) {
        if (!won) {
            return new RewardResponse(0, user.getCoinBalance(), "No reward this time. Try again.");
        }

        int reward = switch (gameType.toLowerCase()) {
            case "tic-tac-toe" -> 25;
            case "arcade-clicker" -> 15;
            default -> 5;
        };
        return addCoins(user, reward, "Mini-game reward: " + gameType, "Mini-game reward added.");
    }

    @Transactional
    public void recordTransaction(AppUser user, int amount, String reason) {
        user.addCoins(amount);
        appUserRepository.save(user);
        transactionRepository.save(new CoinTransaction(user, amount, reason));
    }

    public List<TransactionResponse> getTransactions(AppUser user) {
        return transactionRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getAmount(),
                        transaction.getReason(),
                        transaction.getCreatedAt()
                ))
                .toList();
    }

    private RewardResponse addCoins(AppUser user, int amount, String reason, String message) {
        recordTransaction(user, amount, reason);
        return new RewardResponse(amount, user.getCoinBalance(), message);
    }
}
