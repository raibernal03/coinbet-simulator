package com.coinbet.simulator.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public final class CoinDtos {
    private CoinDtos() {
    }

    public record BalanceResponse(int coinBalance) {
    }

    public record MiniGameResultRequest(@NotBlank String gameType, boolean won) {
    }

    public record RewardResponse(int reward, int coinBalance, String message) {
    }

    public record TransactionResponse(int amount, String reason, LocalDateTime createdAt) {
    }
}
