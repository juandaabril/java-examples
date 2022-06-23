package com.juandaabril.consumer.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Book {

    @Id
    private UUID id;
    private String name;
    private String author;

    public Book() {
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
