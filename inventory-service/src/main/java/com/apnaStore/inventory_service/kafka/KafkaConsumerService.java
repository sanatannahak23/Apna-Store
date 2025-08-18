package com.apnaStore.inventory_service.kafka;

import com.apnaStore.inventory_service.services.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final InventoryService inventoryService;

    @KafkaListener(topics = KafkaTopics.DELETE_INVENTORY, groupId = "my-group")
    public void consume(String message) {
        try {
            log.info("Received message :: {}", message);
            inventoryService.removeInventoryByProductId(message);
        } catch (Exception ex) {
            log.error("Error while processing message: {}", message, ex);
        }
    }
}
