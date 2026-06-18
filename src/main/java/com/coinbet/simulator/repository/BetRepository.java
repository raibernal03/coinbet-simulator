package com.coinbet.simulator.repository;

import com.coinbet.simulator.model.AppUser;
import com.coinbet.simulator.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByUserOrderByCreatedAtDesc(AppUser user);
}
