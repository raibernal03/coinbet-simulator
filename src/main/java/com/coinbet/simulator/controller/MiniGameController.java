package com.coinbet.simulator.controller;

import com.coinbet.simulator.dto.CoinDtos.MiniGameResultRequest;
import com.coinbet.simulator.dto.CoinDtos.RewardResponse;
import com.coinbet.simulator.model.AppUser;
import com.coinbet.simulator.service.CoinService;
import com.coinbet.simulator.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/minigames")
public class MiniGameController {

    private final UserService userService;
    private final CoinService coinService;

    public MiniGameController(UserService userService, CoinService coinService) {
        this.userService = userService;
        this.coinService = coinService;
    }

    @PostMapping("/result")
    public RewardResponse result(@Valid @RequestBody MiniGameResultRequest request, Authentication authentication) {
        AppUser user = userService.getByUsername(authentication.getName());
        return coinService.rewardForMiniGame(user, request.gameType(), request.won());
    }
}
