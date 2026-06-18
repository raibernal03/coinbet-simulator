package com.coinbet.simulator.repository;

import com.coinbet.simulator.model.AppUser;
import com.coinbet.simulator.model.CoinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, Long> {
    List<CoinTransaction> findByUserOrderByCreatedAtDesc(AppUser user);
}
