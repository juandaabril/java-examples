package com.juandaabril.producer.http.controller;

import com.juandaabril.producer.domain.Book;
import com.juandaabril.producer.domain.BookCreated;
import com.juandaabril.producer.http.BookEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
public class SaveANewBookController {

    private Logger logger = LoggerFactory.getLogger(SaveANewBookController.class);

    @Autowired
    private BookEventProducer bookEventProducer;

    @PostMapping("/books/save-a-new-book")
    public ResponseEntity saveANewBook(@RequestBody @Valid SaveANewBookRequest request) {
        Book book = new Book(request.id, request.bookName, request.author);
        BookCreated bookCreated = new BookCreated(null, book);

        logger.info("before send event");
        bookEventProducer.send(bookCreated);
        logger.info("after send event");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/books/save-a-new-book-sync")
    public ResponseEntity saveANewBookSync(@RequestBody @Valid SaveANewBookRequest request) {
        Book book = new Book(request.id, request.bookName, request.author);
        BookCreated bookCreated = new BookCreated(null, book);

        logger.info("before send event");
        bookEventProducer.sendSync(bookCreated);
        logger.info("after send event");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public static class SaveANewBookRequest {
        @NotNull
        public UUID id;

        @NotNull
        public String bookName;

        @NotNull
        public String author;
    }
}


