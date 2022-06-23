package com.juandaabril.consumer.model;

public class BookCreated extends BookEvent {

    private Book book;

    public BookCreated(Integer id, Book book) {
        super(id);
        this.book = book;
    }

    public Book getBook() {
        return book;
    }
}
