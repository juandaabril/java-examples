package com.juandaabril.consumer.model;

public class BookUpdated extends BookEvent {

    private Book book;

    public BookUpdated(Integer id, Book book) {
        super(id);
        this.book = book;
    }

    public Book getBook() {
        return book;
    }
}
