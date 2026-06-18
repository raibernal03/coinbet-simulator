package com.coinbet.simulator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sports_games")
public class SportsGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sport;

    @Column(nullable = false)
    private String homeTeam;

    @Column(nullable = false)
    private String awayTeam;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal homeOdds;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal awayOdds;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @Column(length = 10)
    private String winner;

    protected SportsGame() {
    }

    public SportsGame(String sport, String homeTeam, String awayTeam, BigDecimal homeOdds, BigDecimal awayOdds,
                      LocalDateTime startTime, GameStatus status, String winner) {
        this.sport = sport;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeOdds = homeOdds;
        this.awayOdds = awayOdds;
        this.startTime = startTime;
        this.status = status;
        this.winner = winner;
    }

    public Long getId() {
        return id;
    }

    public String getSport() {
        return sport;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public BigDecimal getHomeOdds() {
        return homeOdds;
    }

    public BigDecimal getAwayOdds() {
        return awayOdds;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public GameStatus getStatus() {
        return status;
    }

    public String getWinner() {
        return winner;
    }
}
