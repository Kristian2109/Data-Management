package com.kris.data_management.services;

import com.kris.no_code_common.KafkaEvents.ColumnDeletedEvent;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaProducerService {

    private final AdminClient adminClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TABLE_ACTIONS_TOPIC_NAME = "table-actions";

    public KafkaProducerService(AdminClient adminClient, KafkaTemplate<String, Object> kafkaTemplate) {
        this.adminClient = adminClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void init() {
        try {
            if (!adminClient.listTopics().names().get().contains(TABLE_ACTIONS_TOPIC_NAME)) {
                NewTopic topic = new NewTopic(TABLE_ACTIONS_TOPIC_NAME, 1, (short) 1);
                adminClient.createTopics(List.of(topic)).all().get();
                System.out.println("Topic created: " + TABLE_ACTIONS_TOPIC_NAME);
            } else {
                System.out.println("Topic already exists: " + TABLE_ACTIONS_TOPIC_NAME);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create topic", e);
        }
    }

    public void sendColumnDeletedEvent(ColumnDeletedEvent event) {
        String key = event.columnId() + ":" + event.tableId();
        kafkaTemplate.send(TABLE_ACTIONS_TOPIC_NAME, key, event).whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("Exception!!!!" + ex.getMessage());
            }
            System.out.println("Message sent: " + result);
        });
    }
}


