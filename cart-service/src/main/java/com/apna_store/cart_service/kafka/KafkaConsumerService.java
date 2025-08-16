package com.apna_store.cart_service.kafka;

import com.apna_store.cart_service.service.services.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final CartService cartService;

    @KafkaListener(topics = KafkaTopics.DELETE_CART, groupId = "my-group")
    public void consume(String message) {
        cartService.deleteCart(Long.parseLong(message));
        log.info("Received message :: {}", message);
    }
}
