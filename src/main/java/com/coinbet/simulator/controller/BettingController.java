package com.coinbet.simulator.controller;

import com.coinbet.simulator.dto.BetDtos.BetResponse;
import com.coinbet.simulator.dto.BetDtos.GameResponse;
import com.coinbet.simulator.dto.BetDtos.PlaceBetRequest;
import com.coinbet.simulator.model.AppUser;
import com.coinbet.simulator.service.BetService;
import com.coinbet.simulator.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BettingController {

    private final BetService betService;
    private final UserService userService;

    public BettingController(BetService betService, UserService userService) {
        this.betService = betService;
        this.userService = userService;
    }

    @GetMapping("/games")
    public List<GameResponse> games() {
        return betService.listGames();
    }

    @PostMapping("/bets")
    public BetResponse placeBet(@Valid @RequestBody PlaceBetRequest request, Authentication authentication) {
        AppUser user = userService.getByUsername(authentication.getName());
        return betService.placeBet(user, request.gameId(), request.pick(), request.amount());
    }

    @GetMapping("/bets/history")
    public List<BetResponse> history(Authentication authentication) {
        AppUser user = userService.getByUsername(authentication.getName());
        return betService.getHistory(user);
    }
}
