package com.coinbet.simulator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "bets")
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sports_game_id")
    private SportsGame sportsGame;

    @Column(nullable = false, length = 10)
    private String pick;

    @Column(nullable = false)
    private int amountWagered;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BetOutcome outcome;

    @Column(nullable = false)
    private int coinsWonLost;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected Bet() {
    }

    public Bet(AppUser user, SportsGame sportsGame, String pick, int amountWagered, BetOutcome outcome, int coinsWonLost) {
        this.user = user;
        this.sportsGame = sportsGame;
        this.pick = pick;
        this.amountWagered = amountWagered;
        this.outcome = outcome;
        this.coinsWonLost = coinsWonLost;
    }

    public Long getId() {
        return id;
    }

    public SportsGame getSportsGame() {
        return sportsGame;
    }

    public String getPick() {
        return pick;
    }

    public int getAmountWagered() {
        return amountWagered;
    }

    public BetOutcome getOutcome() {
        return outcome;
    }

    public int getCoinsWonLost() {
        return coinsWonLost;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
