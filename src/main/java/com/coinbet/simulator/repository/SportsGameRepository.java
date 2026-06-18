package com.coinbet.simulator.repository;

import com.coinbet.simulator.model.SportsGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportsGameRepository extends JpaRepository<SportsGame, Long> {
}
