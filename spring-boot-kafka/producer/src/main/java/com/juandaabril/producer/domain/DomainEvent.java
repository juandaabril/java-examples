package com.juandaabril.producer.domain;

public class DomainEvent {

    private Integer id;
    private String type;

    public DomainEvent(Integer id) {
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
