package com.borislavmm.walletservice.messaging;

import com.borislavmm.walletservice.messaging.messages.DeletePersonMessage;
import com.borislavmm.walletservice.model.Wallet;
import com.borislavmm.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AmqpConfig {

    private final WalletRepository walletRepository;

    @Bean
    public Queue moneyTransfer() {
        return new Queue("money-transfer", false);
    }
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

    @RabbitListener(queues = "person-delete")
    public void listenPersonDelete(DeletePersonMessage request) {
        Wallet wallet = walletRepository.getWalletByNumber(request.getPersonWalletNumber()).get();
        walletRepository.delete(wallet);
        log.info("Person was deleted, deleting wallet: " + wallet.getWalletNumber());
    }
}
