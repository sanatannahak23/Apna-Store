package com.apnaStore.product_catalog_service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void messageForDeleteInventory(Long message) {
        kafkaTemplate.send(KafkaTopics.DELETE_INVENTORY, message.toString());
        log.info("Sent message :: {} ", message);
    }
}
