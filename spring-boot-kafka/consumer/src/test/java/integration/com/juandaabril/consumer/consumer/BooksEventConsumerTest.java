package com.juandaabril.consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.juandaabril.consumer.model.Book;
import com.juandaabril.consumer.model.BookCreated;
import com.juandaabril.consumer.model.BookUpdated;
import com.juandaabril.consumer.repository.BookRepository;
import com.juandaabril.consumer.service.BookService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class BooksEventConsumerTest extends ConsumerKafkaIntegrationTest{

    @SpyBean(reset = MockReset.AFTER)
    BooksEventConsumer booksEventConsumer;

    @SpyBean(reset = MockReset.AFTER)
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void should_create_a_new_book() throws JsonProcessingException, InterruptedException {
        BookCreated bookCreated = new BookCreated(null, new Book(UUID.randomUUID(), "Any Name", "Any"));
        String json = objectMapper.writeValueAsString(bookCreated);

        kafkaTemplate.send("books", json);

        CountDownLatch latch = new CountDownLatch(1);
        latch.await(3, TimeUnit.SECONDS);

        verify(booksEventConsumer, times(1)).onMessage(isA(ConsumerRecord.class));
        verify(bookService, times(1)).newBook(isA(Book.class));

        List<Book> books = (List<Book>) bookRepository.findAll();
        assert books.size() ==1;
        books.forEach(book -> {
            assert book.getId()!=null;
            assertEquals(bookCreated.getBook().getId(), book.getId());
        });
    }

    @Test
    void should_update_a_book() throws JsonProcessingException, InterruptedException {
        Book book = new Book(UUID.randomUUID(), "Any Name", "Any");
        bookRepository.save(book);

        BookUpdated bookUpdated = new BookUpdated(null, new Book(book.getId(), "Other Name", "Other Author"));
        String json = objectMapper.writeValueAsString(bookUpdated);

        kafkaTemplate.send("books", json);

        CountDownLatch latch = new CountDownLatch(1);
        latch.await(3, TimeUnit.SECONDS);

        //verify(booksEventConsumer, times(1)).onMessage(isA(ConsumerRecord.class));
        //verify(bookService, times(1)).updateBook(isA(Book.class));

        Book bookDB = bookRepository.findById(book.getId()).get();

        assertEquals(bookUpdated.getBook().getId(), bookDB.getId());
        assertEquals(bookUpdated.getBook().getAuthor(), bookDB.getAuthor());
        assertEquals(bookUpdated.getBook().getName(), bookDB.getName());
    }

    @Test
    void should_throw_error_when_book_doesnt_exists() throws JsonProcessingException, InterruptedException {
        Book book = new Book(UUID.randomUUID(), "Any Name", "Any");

        BookUpdated bookUpdated = new BookUpdated(null, new Book(book.getId(), "Other Name", "Other Author"));
        String json = objectMapper.writeValueAsString(bookUpdated);

        kafkaTemplate.send("books", json);

        CountDownLatch latch = new CountDownLatch(1);
        latch.await(3, TimeUnit.SECONDS);

        verify(booksEventConsumer, times(1)).onMessage(isA(ConsumerRecord.class));
        verify(bookService, times(1)).updateBook(isA(Book.class));
    }


    @Test
    void should_throw_error_when_book_id_not_exists() throws JsonProcessingException, InterruptedException {
        BookUpdated bookUpdated = new BookUpdated(null, new Book(null, "Other Name", "Other Author"));
        String json = objectMapper.writeValueAsString(bookUpdated);

        kafkaTemplate.send("books", json);

        CountDownLatch latch = new CountDownLatch(1);
        latch.await(3, TimeUnit.SECONDS);

        verify(booksEventConsumer, times(3)).onMessage(isA(ConsumerRecord.class));
        verify(bookService, times(3)).updateBook(isA(Book.class));
    }
}
