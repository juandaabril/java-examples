package com.juandaabril.producer.http.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.juandaabril.producer.domain.BookCreated;
import com.juandaabril.producer.http.controller.SaveANewBookController.SaveANewBookRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SaveANewBookControllerIntegrationTest extends  KafkaIntegrationTest{

    @Test
    public void should_create_a_new_book() {
        SaveANewBookRequest request = new SaveANewBookRequest();
        request.id = UUID.randomUUID();
        request.bookName = "Any BookName";
        request.author = "Any Author";

        HttpEntity<SaveANewBookRequest> httpRequest = new HttpEntity<>(request);

        ResponseEntity<Void> response = restTemplate.exchange(
            "/books/save-a-new-book",
            HttpMethod.POST,
            httpRequest, Void.class
        );

        assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());

        ConsumerRecord<Integer, String> record = KafkaTestUtils.getSingleRecord(consumer, "books");
        DocumentContext jsonPath = JsonPath.parse(record.value());

        assertThat(request.id.toString()).isEqualTo(jsonPath.read("$.book.id"));
        assertThat(request.bookName).isEqualTo(jsonPath.read("$.book.name"));
        assertThat(request.author).isEqualTo(jsonPath.read("$.book.author"));
        assertThat(BookCreated.class.getSimpleName()).isEqualTo(jsonPath.read("$.type"));
    }

}
