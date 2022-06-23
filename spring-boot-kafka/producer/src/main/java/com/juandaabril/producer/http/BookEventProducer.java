package com.juandaabril.producer.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juandaabril.producer.domain.DomainEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

@Component
public class BookEventProducer {

    private Logger logger = LoggerFactory.getLogger(BookEventProducer.class);

    public static String TOPIC = "books";
    private final KafkaTemplate<Integer, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public BookEventProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public ListenableFuture<SendResult<Integer,String>> send(DomainEvent event) {
        try {
            Integer key = event.getId();
            String payload = objectMapper.writeValueAsString(event);

            ListenableFuture<SendResult<Integer,String>> listenableFuture =  kafkaTemplate.send(TOPIC, event.getId(), payload);

            listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
                @Override
                public void onFailure(Throwable ex) {
                    logger.info("error - {}",  ex.getMessage());
                    throw new RuntimeException(ex);
                }

                @Override
                public void onSuccess(SendResult<Integer, String> result) {
                    logger.info("send - key: {} , payload: {},  partition: {}",  key, payload,  result.getRecordMetadata().partition());
                }
            });

            return listenableFuture;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public  SendResult<Integer, String> sendSync(DomainEvent event) {
        try {
            Integer key = event.getId();
            String payload = objectMapper.writeValueAsString(event);

            SendResult<Integer, String> result = kafkaTemplate.send(TOPIC, 1, payload).get();

            logger.info("sendSync - key: {} , payload: {},  partition: {}",  key, payload,  result.getRecordMetadata().partition());

            return result;
        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public ListenableFuture<SendResult<Integer,String>> sendSyncWithProducerRecord(DomainEvent event) {
        try {
            Integer key = event.getId();
            String payload = objectMapper.writeValueAsString(event);

            ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>(
                TOPIC,
                null,
                key,
                payload
            );

            ListenableFuture<SendResult<Integer,String>> listenableFuture =  kafkaTemplate.send(producerRecord);
            listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
                @Override
                public void onFailure(Throwable ex) {
                    logger.info("error - {}",  ex.getMessage());
                    throw new RuntimeException("ex");
                }

                @Override
                public void onSuccess(SendResult<Integer, String> result) {
                    logger.info("send - key: {} , payload: {},  partition: {}",  key, payload,  result.getRecordMetadata().partition());
                }
            });

            return listenableFuture;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
