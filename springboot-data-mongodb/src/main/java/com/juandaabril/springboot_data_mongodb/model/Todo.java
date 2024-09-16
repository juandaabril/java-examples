package com.juandaabril.springboot_data_mongodb.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "todos")
@Data
public class Todo {
    @Id
    private String id;
    private String title;
    private String description;
    private LocalDate date;
    private List<Item> items;
}
