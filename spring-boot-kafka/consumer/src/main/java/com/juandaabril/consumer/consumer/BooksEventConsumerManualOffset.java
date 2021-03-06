package com.juandaabril.consumer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

//@Component
public class BooksEventConsumerManualOffset implements AcknowledgingMessageListener<Integer, String> {

    private Logger logger = LoggerFactory.getLogger(BooksEventConsumerManualOffset.class);

    @KafkaListener(topics = {"books"})
    @Override
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
        logger.info("ConsumerRecord: {}", consumerRecord);

        acknowledgment.acknowledge();
    }

}
