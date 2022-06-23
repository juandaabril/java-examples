package com.juandaabril.producer.http.controller;

import com.juandaabril.producer.domain.Book;
import com.juandaabril.producer.domain.BookCreated;
import com.juandaabril.producer.http.BookEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UpdateABookController {

    private Logger logger = LoggerFactory.getLogger(UpdateABookController.class);

    @Autowired
    private BookEventProducer bookEventProducer;

    @PutMapping("/books/update-book/{id}")
    public ResponseEntity handle(@PathVariable UUID id, @RequestBody UpdateBookRequest request) {
        Book book = new Book(id, request.bookName, request.author);
        BookCreated bookCreated = new BookCreated(null, book);

        logger.info("before send event");
        bookEventProducer.send(bookCreated);
        logger.info("after send event");

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public static class UpdateBookRequest {
        public String bookName;
        public String author;
    }
}


