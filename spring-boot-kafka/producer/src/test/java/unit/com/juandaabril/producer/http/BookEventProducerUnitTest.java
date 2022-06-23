package com.juandaabril.producer.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juandaabril.producer.domain.Book;
import com.juandaabril.producer.domain.BookCreated;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.network.Send;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookEventProducerUnitTest {

    @Mock
    private KafkaTemplate<Integer, String> kafkaTemplate;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private BookEventProducer bookEventProducer;

    @Test
    public void should_fail_send_a_event() {
        Book book = new Book(UUID.randomUUID(), "Any Name", "Any Author");
        BookCreated bookCreated = new BookCreated(1, book);

        SettableListenableFuture future = new SettableListenableFuture();
        future.setException(new RuntimeException("Exception calling kafka"));

        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);

        assertThrows(ExecutionException.class, () -> bookEventProducer.sendSyncWithProducerRecord(bookCreated).get());
    }

    @Test
    public void should_fail_send_a_success() throws JsonProcessingException, ExecutionException, InterruptedException {
        Book book = new Book(UUID.randomUUID(), "Any Name", "Any Author");
        BookCreated bookCreated = new BookCreated(1, book);


        String record = objectMapper.writeValueAsString(bookCreated);
        SettableListenableFuture future = new SettableListenableFuture();
        ProducerRecord<Integer, String> producerRecord = new ProducerRecord(BookEventProducer.TOPIC, bookCreated.getId(), record);
        RecordMetadata recordMetadata = new RecordMetadata(
            new TopicPartition(BookEventProducer.TOPIC, 1),
            1,
            1,
            345,
            1,
            2
        );

        SendResult<Integer, String> sendResult = new SendResult<>(producerRecord, recordMetadata);
        future.set(sendResult);

        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);

        ListenableFuture<SendResult<Integer, String>> listenableFuture = bookEventProducer.sendSyncWithProducerRecord(bookCreated);

        SendResult<Integer, String> result= listenableFuture.get();

        assertThat(result.getRecordMetadata().partition()).isEqualTo(1);
    }
}
