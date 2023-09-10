package com.borislavmm.walletservice.service;

import com.borislavmm.walletservice.dto.ExchangePairResponse;
import com.borislavmm.walletservice.messaging.messages.DeleteWalletMessage;
import com.borislavmm.walletservice.messaging.messages.TransactionRequest;
import com.borislavmm.walletservice.model.CurrencyInformation;
import com.borislavmm.walletservice.model.Wallet;
import com.borislavmm.walletservice.repository.CurrencyInformationRepository;
import com.borislavmm.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletService {

    private final RabbitTemplate rabbitTemplate;
    private final WalletRepository walletRepository;
    private final CurrencyInformationRepository currencyInformationRepository;
    private final WebClient.Builder webClient;

    public String addMoney(String walletNumber, String currencyCode, double amount) {
        Wallet wallet = walletRepository.getWalletByNumber(walletNumber).orElseGet(() -> new Wallet(walletNumber));

        CurrencyInformation currencyInformation = wallet.getCurrencies().stream()
                .filter(c -> c.getCurrencyCode().equals(currencyCode))
                .findFirst()
                .orElse(new CurrencyInformation(currencyCode,0,0));
        currencyInformation.addAvailableAmount(amount);
        wallet.addCurrency(currencyInformation);
        walletRepository.save(wallet);
        return "Success";
    }

    public String takeMoney(String walletNumber, String currencyCode, double amount) {
        Wallet wallet = getWallet(walletNumber);
        CurrencyInformation currencyInformation = wallet.getCurrencies().stream()
                .filter(c -> c.getCurrencyCode().equals(currencyCode))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Wallet doesn't have registered currency: " + currencyCode));

        currencyInformation.takeAvailableAmount(amount);
        walletRepository.save(wallet);

        return "Success";
    }

    public String exchangeMoney(String walletNumber, String sourceCurrency, String targetSource,
                                double exchangeRate, double amount) {
        Wallet wallet = getWallet(walletNumber);

        CurrencyInformation sourceCurrencyInformation = getCurrencyByCode(wallet, sourceCurrency)
                .orElseThrow(() -> new NoSuchElementException("Wallet doesn't have registered currency: " + sourceCurrency));

        CurrencyInformation targetCurrencyInformation = getCurrencyByCode(wallet, targetSource)
                .orElse(new CurrencyInformation(targetSource,0,0));

        sourceCurrencyInformation.takeAvailableAmount(amount);
        targetCurrencyInformation.addAvailableAmount(amount*exchangeRate);
        wallet.addCurrency(targetCurrencyInformation);

        walletRepository.save(wallet);
        return "Success";
    }

    public String removeCurrencyInformation(String walletNumber, String currencyCode) {
        Wallet wallet = getWallet(walletNumber);
        CurrencyInformation currencyInformation = getCurrencyByCode(wallet, currencyCode)
                .orElseThrow(() -> new NoSuchElementException("Currency with code: " + currencyCode +
                        "was not found in wallet: " + wallet.getWalletNumber() ));

        wallet.getCurrencies().remove(currencyInformation);
        currencyInformationRepository.delete(currencyInformation);

        return "Success";
    }

    public String removeWallet(String walletNumber) {
        Wallet wallet = getWallet(walletNumber);
        walletRepository.delete(wallet);

        rabbitTemplate.convertAndSend("wallet-delete", new DeleteWalletMessage(walletNumber));

        return "Success";
    }

    private Wallet getWallet(String walletNumber) {
        return walletRepository.getWalletByNumber(walletNumber)
                .orElseThrow(() -> new NoSuchElementException("Wallet was not found: " + walletNumber));
    }

    private Optional<CurrencyInformation> getCurrencyByCode(Wallet wallet, String currencyCode) {
        return wallet.getCurrencies().stream()
                .filter(c -> c.getCurrencyCode().equals(currencyCode))
                .findFirst();
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
    public String  transferMoney(String senderWallet, String receiverWallet,
                                 String currencyCodeSend, String currencyCodeReceive, double amount) {

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
        this.takeMoney(senderWallet, currencyCodeSend, amount);
        this.addMoney(receiverWallet, currencyCodeReceive, amount*exchangeRate);

        TransactionRequest message = buildTransaction(senderWallet, receiverWallet, "Money transfer",
                currencyCodeReceive, amount*exchangeRate);

        rabbitTemplate.convertAndSend("money-transfer", message);
        return "Success";
    }
}
