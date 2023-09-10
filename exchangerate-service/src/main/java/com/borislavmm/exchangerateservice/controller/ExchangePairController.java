package com.borislavmm.exchangerateservice.controller;

import com.borislavmm.exchangerateservice.dto.ExchangePairResponse;
import com.borislavmm.exchangerateservice.service.ExchangePairService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/pair")
@RequiredArgsConstructor
public class ExchangePairController {

    private final ExchangePairService pairService;

    @GetMapping("/{base}/{target}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ExchangePairResponse> getExchangeRate(@PathVariable String base,
                                                                   @PathVariable String target,
                                                                   @RequestParam(required = false, name = "amount") Double amount) {
        return CompletableFuture.supplyAsync(() -> pairService.getExchangeRate(base, target, (amount==null?0:amount)));
    }


}
