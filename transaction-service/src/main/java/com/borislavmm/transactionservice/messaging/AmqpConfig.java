package com.borislavmm.transactionservice.messaging;

import com.borislavmm.transactionservice.dto.TransactionRequest;
import com.borislavmm.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class AmqpConfig {

    private final TransactionService transactionService;

    @Bean
    public Queue defaultQueue() {
        return new Queue("money-transfer", false);
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        return converter;
    }

    @RabbitListener(queues = "money-transfer")
    public void listen(TransactionRequest request) {
        transactionService.saveTransaction(LocalDateTime.now(), request);
        log.info("Transaction saved: {}", request);
    }

}
