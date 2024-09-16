package com.juandaabril.springboot_data_mongodb.model;

import lombok.Data;

@Data
public class Item {
    private String description;
    private boolean completed;
}
