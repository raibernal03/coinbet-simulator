package com.coinbet.simulator.controller;

import com.coinbet.simulator.dto.CoinDtos.BalanceResponse;
import com.coinbet.simulator.dto.CoinDtos.RewardResponse;
import com.coinbet.simulator.dto.CoinDtos.TransactionResponse;
import com.coinbet.simulator.model.AppUser;
import com.coinbet.simulator.service.CoinService;
import com.coinbet.simulator.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/coins")
public class CoinController {

    private final UserService userService;
    private final CoinService coinService;

    public CoinController(UserService userService, CoinService coinService) {
        this.userService = userService;
        this.coinService = coinService;
    }

    @GetMapping("/balance")
    public BalanceResponse balance(Authentication authentication) {
        AppUser user = currentUser(authentication);
        return new BalanceResponse(user.getCoinBalance());
    }

    @PostMapping("/watch-ad")
    public RewardResponse watchAd(Authentication authentication) {
        return coinService.rewardForAd(currentUser(authentication));
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> transactions(Authentication authentication) {
        return coinService.getTransactions(currentUser(authentication));
    }

    private AppUser currentUser(Authentication authentication) {
        return userService.getByUsername(authentication.getName());
    }
}
