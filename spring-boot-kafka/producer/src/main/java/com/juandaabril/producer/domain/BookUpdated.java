package com.juandaabril.producer.domain;

public class BookUpdated extends DomainEvent {

    private Book book;

    public BookUpdated(Integer id, Book book) {
        super(id);
        this.book = book;
    }

    public Book getBook() {
        return book;
    }
}
