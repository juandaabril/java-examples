package com.juandaabril.producer.http.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.juandaabril.producer.domain.BookCreated;
import com.juandaabril.producer.http.controller.UpdateABookController.UpdateBookRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateABookControllerIntegrationTest extends KafkaIntegrationTest{

    @Test
    public void should_update_a_book() {
        UpdateBookRequest request = new UpdateBookRequest();
        UUID id = UUID.randomUUID();
        request.bookName = "Any BookName";
        request.author = "Any Author";

        HttpEntity<UpdateBookRequest> httpRequest = new HttpEntity<>(request);

        ResponseEntity<Void> response = restTemplate.exchange(
            String.format("/books/update-book/%s", id.toString()),
            HttpMethod.PUT,
            httpRequest,
            Void.class
        );

        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());

        ConsumerRecord<Integer, String> record = KafkaTestUtils.getSingleRecord(consumer, "books");
        DocumentContext jsonPath = JsonPath.parse(record.value());

        assertThat(id.toString()).isEqualTo(jsonPath.read("$.book.id"));
        assertThat(request.bookName).isEqualTo(jsonPath.read("$.book.name"));
        assertThat(request.author).isEqualTo(jsonPath.read("$.book.author"));
        assertThat(BookCreated.class.getSimpleName()).isEqualTo(jsonPath.read("$.type"));
    }
}
