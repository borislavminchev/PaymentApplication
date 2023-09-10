package com.borislavmm.personservice.messaging;

import com.borislavmm.personservice.messaging.messages.DeleteWalletMessage;
import com.borislavmm.personservice.model.Person;
import com.borislavmm.personservice.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class AmqpConfig {

    private final PersonRepository personRepository;

    @Bean
    public Queue walletDelete() {
        return new Queue("wallet-delete", false);
    }
    @Bean
    public Queue personDelete() {
        return new Queue("person-delete", false);
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        return converter;
    }

    @RabbitListener(queues = "wallet-delete")
    public void listenWalletDelete(DeleteWalletMessage request) {
        List<Person> people = personRepository.findByWallet(request.getWalletNumber()).get();
        personRepository.deleteAll(people);
        log.info("Wallet was deleted, deleting people: " +
                people.stream()
                        .map(p -> p.getName())
                        .collect(Collectors.joining(", ")));
    }
}
