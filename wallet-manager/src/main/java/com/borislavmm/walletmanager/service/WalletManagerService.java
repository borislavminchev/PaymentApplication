package com.borislavmm.walletmanager.service;

import com.borislavmm.walletmanager.dto.CurrencyInfoRequest;
import com.borislavmm.walletmanager.dto.CurrencyRequest;
import com.borislavmm.walletmanager.dto.ExchangePairResponse;
import com.borislavmm.walletmanager.dto.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WalletManagerService {

    private final RabbitTemplate rabbitTemplate;
    private final WebClient.Builder webClient;

    public String transferMoney(String senderWallet, String receiverWallet,
                                String currencyCodeSend,String currencyCodeReceive, double amount) {

        double exchangeRate = 1;
        if(!currencyCodeSend.equals(currencyCodeReceive)) {
            String uri = String.format("http://exchange-rate-service/api/pair/%s/%s", currencyCodeSend, currencyCodeReceive);
            exchangeRate = webClient.build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(ExchangePairResponse.class)
                    .block()
                    .getExchangeRate();

        }

        webClient.build()
                .post()
                .uri("http://wallet-service/api/wallet/take")
                .bodyValue(new CurrencyRequest(currencyCodeSend, amount, senderWallet))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();

        webClient.build()
                .post()
                .uri("http://wallet-service/api/wallet/add")
                .bodyValue(new CurrencyRequest(currencyCodeReceive, amount*exchangeRate, receiverWallet))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();

        TransactionRequest message = buildTransaction(senderWallet, receiverWallet, "Money transfer",
                currencyCodeReceive, amount*exchangeRate);

        rabbitTemplate.convertAndSend("manager-req-sent", message);
        System.out.println("message sent");
        return "Done!";
    }

    public String createWallet(String walletNumber, String initialCurrencyCode) {
        webClient.build()
                .post()
                .uri("http://wallet-service/api/wallet/add")
                .bodyValue(new CurrencyRequest(initialCurrencyCode, 0, walletNumber))
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(String.class)
                .subscribe();

        return "Done";
    }

    public String loadWallet(CurrencyRequest currency) {
        webClient.build()
                .post()
                .uri("http://wallet-service/api/wallet/add")
                .bodyValue(currency)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();

        return "Done!";
    }

    public String takeMoney(CurrencyRequest currencyRequest) {
        webClient.build()
                .post()
                .uri("http://wallet-service/api/wallet/take")
                .bodyValue(currencyRequest)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();

        return "Done!";
    }

    public String deleteCurrency(CurrencyInfoRequest currencyInfo) {
        webClient.build()
                .post()
                .uri("http://wallet-service/api/wallet/remove-currency")
                .bodyValue(currencyInfo)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
        return "Done!";
    }

    public String deleteWallet(String walletNumber) {
        webClient.build()
                .post()
                .uri("http://wallet-service/api/wallet/remove-wallet")
                .bodyValue(walletNumber)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
        return "Done!";
    }

    private static TransactionRequest buildTransaction(String sender, String receiver,
                                                       String description, String currencyCode, double amount) {
        return TransactionRequest.builder()
                .walletSender(sender)
                .walletReceiver(receiver)
                .description(description)
                .currencyCode(currencyCode)
                .amount(amount)
                .build();
    }
}
