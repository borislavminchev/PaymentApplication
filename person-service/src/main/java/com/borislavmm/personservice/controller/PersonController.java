package com.borislavmm.personservice.controller;

import com.borislavmm.personservice.dto.FriendRequest;
import com.borislavmm.personservice.dto.PersonTransaction;
import com.borislavmm.personservice.dto.WalletTransaction;
import com.borislavmm.personservice.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping("/new-friend")
    public CompletableFuture<String> makeNewFriends(@RequestBody FriendRequest friendRequest) {
        return CompletableFuture.supplyAsync(() ->
                personService.createNewFriend(friendRequest.getPersonId1(), friendRequest.getPersonId1()));
    }

    @PostMapping("/send-money")
    public CompletableFuture<String> sendMoney(@RequestBody PersonTransaction personTransaction) {
        return CompletableFuture.supplyAsync(() -> personService.transferMoney(personTransaction));
    }

    @GetMapping("/last-transactions/{id}")
    public CompletableFuture<List<WalletTransaction>> getLastTenTransact(@PathVariable long id) {
        return CompletableFuture.supplyAsync(() -> personService.getLastTenTransactions(id));
    }
}
