package com.coinbet.simulator.dto;

import com.coinbet.simulator.model.BetOutcome;
import com.coinbet.simulator.model.GameStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class BetDtos {
    private BetDtos() {
    }

    public record GameResponse(Long id, String sport, String homeTeam, String awayTeam, BigDecimal homeOdds,
                               BigDecimal awayOdds, LocalDateTime startTime, GameStatus status, String winner) {
    }

    public record PlaceBetRequest(@NotNull Long gameId, @NotBlank String pick, @Min(1) int amount) {
    }

    public record BetResponse(Long id, String game, String pick, int amountWagered, BetOutcome outcome,
                              int coinsWonLost, LocalDateTime createdAt) {
    }
}
