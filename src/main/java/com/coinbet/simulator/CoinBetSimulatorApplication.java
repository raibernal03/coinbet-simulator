package com.coinbet.simulator;

import com.coinbet.simulator.model.GameStatus;
import com.coinbet.simulator.model.SportsGame;
import com.coinbet.simulator.repository.SportsGameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
public class CoinBetSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoinBetSimulatorApplication.class, args);
    }

    @Bean
    CommandLineRunner seedGames(SportsGameRepository sportsGameRepository) {
        return args -> {
            if (sportsGameRepository.count() > 0) {
                return;
            }

            sportsGameRepository.save(new SportsGame(
                    "Football",
                    "Springfield Sharks",
                    "Metro City Bears",
                    new BigDecimal("1.85"),
                    new BigDecimal("2.10"),
                    LocalDateTime.now().plusDays(1),
                    GameStatus.SCHEDULED,
                    null
            ));
            sportsGameRepository.save(new SportsGame(
                    "Basketball",
                    "Lakeside Lynx",
                    "Hilltown Hawks",
                    new BigDecimal("1.65"),
                    new BigDecimal("2.40"),
                    LocalDateTime.now().minusHours(2),
                    GameStatus.FINISHED,
                    "HOME"
            ));
            sportsGameRepository.save(new SportsGame(
                    "Football",
                    "Riverside Rangers",
                    "Capital Kings",
                    new BigDecimal("2.25"),
                    new BigDecimal("1.72"),
                    LocalDateTime.now().minusDays(1),
                    GameStatus.FINISHED,
                    "AWAY"
            ));
        };
    }
}
