package com.juandaabril.producer.domain;

public class BookCreated extends DomainEvent {

    private Book book;

    public BookCreated(Integer id, Book book) {
        super(id);
        this.book = book;
    }

    public Book getBook() {
        return book;
    }
}
