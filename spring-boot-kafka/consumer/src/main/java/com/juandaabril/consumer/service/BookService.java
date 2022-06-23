package com.juandaabril.consumer.service;

import com.juandaabril.consumer.model.Book;
import com.juandaabril.consumer.model.DataNotFound;
import com.juandaabril.consumer.repository.BookRepository;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void newBook(Book book) {
        bookRepository.save(book);
    }

    public void updateBook(Book book) {
        if(book.getId() == null) {
            throw new RecoverableDataAccessException("only for test retry");
        }

        Optional<Book> optionalBook = bookRepository.findById(book.getId());
        if (optionalBook.isEmpty()) {
            throw new DataNotFound(MessageFormat.format("Book with id {0} not fund", book.getId().toString()));
        }

        Book dbBook = optionalBook.get();
        dbBook.setAuthor(book.getAuthor());
        dbBook.setName(book.getName());

        bookRepository.save(book);
    }
}
