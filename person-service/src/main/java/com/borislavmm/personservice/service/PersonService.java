package com.borislavmm.personservice.service;

import com.borislavmm.personservice.dto.MoneyTransferRequest;
import com.borislavmm.personservice.dto.PersonTransaction;
import com.borislavmm.personservice.dto.TransactionRequest;
import com.borislavmm.personservice.dto.WalletTransaction;
import com.borislavmm.personservice.messaging.messages.DeletePersonMessage;
import com.borislavmm.personservice.model.Person;
import com.borislavmm.personservice.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final WebClient.Builder webClient;
    private final PersonRepository personRepository;
    private final RabbitTemplate rabbitTemplate;

    public void deletePerson(Person person) {
        personRepository.delete(person);
        rabbitTemplate.convertAndSend("person-delete", new DeletePersonMessage(person.getWallet()));
    }

    public void deleteAllPeople(Iterable<? extends Person> people) {
        for (Person person : people) {
            deletePerson(person);
        }
    }

    public List<Person> getPeopleByWallet(String walletNumber) {
        return personRepository.findByWallet(walletNumber)
                .orElseThrow(() -> new NoSuchElementException("Wallet with number not found:" + walletNumber));
    }

    public String createNewFriend(Long personId1, Long personId2) {
        Person person1 = getPerson(personId1);
        Person person2 = getPerson(personId2);

        person1.addFriend(person2);
        personRepository.save(person1);
        return "Success";
    }

    public List<WalletTransaction> getLastTenTransactions(Long personId) {
        String walletNumber = getPerson(personId).getWallet();
        return webClient.build()
                .get()
                .uri("http://transaction-service/api/transaction/last-ten/" + walletNumber)
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new))
                .toEntityList(WalletTransaction.class)
                .block()
                .getBody();
    }

    public String transferMoney(PersonTransaction personTransaction) {
        Person sender = getPerson(personTransaction.getSenderId());
        Person receiver = getPerson(personTransaction.getReceiverId());

        MoneyTransferRequest transactionRequest = MoneyTransferRequest.builder()
                .senderWallet(sender.getWallet())
                .receiverWallet(receiver.getWallet())
                .currencyCodeSend(personTransaction.getCurrencyCode())
                .currencyCodeReceive(personTransaction.getCurrencyCode())
                .description(personTransaction.getDescription())
                .amount(personTransaction.getAmount())
                .build();

        webClient.build()
                .post()
                .uri("http://wallet-service/api/wallet/transfer")
                .bodyValue(transactionRequest)
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(String.class)
                .subscribe();

        return "Success";
    }



    private Person getPerson(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new NoSuchElementException("Person not found:" + personId));
    }
}
