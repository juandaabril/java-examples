package com.juandaabril.consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juandaabril.consumer.model.BookCreated;
import com.juandaabril.consumer.model.BookEvent;
import com.juandaabril.consumer.model.BookUpdated;
import com.juandaabril.consumer.service.BookService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BooksEventConsumer {

    private Logger logger = LoggerFactory.getLogger(BooksEventConsumer.class);
    private ObjectMapper objectMapper;
    private BookService bookService;

    public BooksEventConsumer(ObjectMapper objectMapper, BookService bookService) {
        this.objectMapper = objectMapper;
        this.bookService = bookService;
    }

    @KafkaListener(topics = {"books"})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        logger.info("ConsumerRecord: {}", consumerRecord);
        BookEvent bookEvent = objectMapper.readValue(consumerRecord.value(), BookEvent.class);
        if (bookEvent instanceof BookCreated) {
            BookCreated bookCreated = (BookCreated) bookEvent;
            bookService.newBook(bookCreated.getBook());
            return;
        }

        if (bookEvent instanceof BookUpdated) {
            BookUpdated bookUpdated = (BookUpdated) bookEvent;
            bookService.updateBook(bookUpdated.getBook());
            return;
        }

    }

}
