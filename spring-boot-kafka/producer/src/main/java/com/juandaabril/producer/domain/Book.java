package com.juandaabril.producer.domain;

import java.util.UUID;

public class Book {

    private UUID id;
    private String name;
    private String author;

    public Book(UUID id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }
}
