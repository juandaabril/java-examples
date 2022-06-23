package com.juandaabril.consumer.model;

public class BookEvent {

    private Integer id;
    private String type;

    public BookEvent(Integer id) {
        this.id = id;
        this.type = this.getClass().getSimpleName();
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
